// DriveView.cpp : implementation of the CDriveView class
//

#include "stdafx.h"
#include "Explorer.h"

#include "ExplorerDoc.h"
#include "DriveView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CDriveView

IMPLEMENT_DYNCREATE(CDriveView, CTreeView)

BEGIN_MESSAGE_MAP(CDriveView, CTreeView)
	//{{AFX_MSG_MAP(CDriveView)
	ON_NOTIFY_REFLECT(TVN_ITEMEXPANDING, OnItemexpanding)
	ON_NOTIFY_REFLECT(TVN_SELCHANGED, OnSelchanged)
	ON_WM_CREATE()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDriveView construction/destruction

CDriveView::CDriveView()
{
	// TODO: add construction code here

}

CDriveView::~CDriveView()
{
}

BOOL CDriveView::PreCreateWindow(CREATESTRUCT& cs)
{
	cs.style |= TVS_HASLINES | TVS_LINESATROOT | TVS_HASBUTTONS | TVS_SHOWSELALWAYS;

	return CTreeView::PreCreateWindow(cs);
}

int CDriveView::OnCreate(LPCREATESTRUCT lpCreateStruct) 
{
	if (CTreeView::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	// 이미지 리스트 생성
	m_imgDrives.Create(IDB_DRIVES, 16, 1, RGB(0, 128, 128));
	// 이미지 리스트와 트리 컨트롤 연결
	GetTreeCtrl().SetImageList(&m_imgDrives, TVSIL_NORMAL);
	
	return 0;
}

/////////////////////////////////////////////////////////////////////////////
// CDriveView drawing

void CDriveView::OnDraw(CDC* pDC)
{
	CExplorerDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);

	// TODO: add draw code for native data here
}


void CDriveView::OnInitialUpdate()
{
	CTreeView::OnInitialUpdate();
	InitDriveView();
}

/////////////////////////////////////////////////////////////////////////////
// CDriveView diagnostics

#ifdef _DEBUG
void CDriveView::AssertValid() const
{
	CTreeView::AssertValid();
}

void CDriveView::Dump(CDumpContext& dc) const
{
	CTreeView::Dump(dc);
}

CExplorerDoc* CDriveView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CExplorerDoc)));
	return (CExplorerDoc*)m_pDocument;
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CDriveView message handlers

// 시스템에 설치된 디스크 드라이브 정보 얻기
int CDriveView::InitDriveView()
{
	int nPos = 0;
	int nDrives = 0;
	CString strDrive = "?:\\";

	// 현재 시스템에 설치되어 있는 디스크 드라이브 정보 얻기
	DWORD dwDriveList = ::GetLogicalDrives();

	while(dwDriveList)
	{
		if(dwDriveList & 1)
		{
			// "C:\\"과 같이 드라이브를 표시하는 문자열을 만듦
			strDrive.SetAt(0, 'A'+nPos);
			// 드라이브 문자열을 트리 컨트롤에 노드로 추가
			if(AddDriveNode(strDrive))
				nDrives++;
		}
		// 우로 1비트 이동
		dwDriveList >>= 1;
		nPos++;
	}
	return nDrives;
}


// 드라이브 문자열을 트리 컨트롤의 노드로 추가
BOOL CDriveView::AddDriveNode(CString &strDrive)
{
	CString str;
	HTREEITEM hItem;
	static BOOL bFirst = TRUE;

	// 디스크 드라이브의 타입을 얻음
	UINT nType = ::GetDriveType((LPCTSTR)strDrive);

	switch(nType)
	{
		// 플로피 디스크 드라이브인 경우
		case DRIVE_REMOVABLE:
			hItem = GetTreeCtrl().InsertItem(strDrive, ID_FLOPPY, ID_FLOPPY);
			GetTreeCtrl().InsertItem("", ID_CLOSEDFOLDER, ID_CLOSEDFOLDER, hItem);
			break;
		// 하드 디스크 드라이브인 경우
		case DRIVE_FIXED:
			hItem = GetTreeCtrl().InsertItem(strDrive, ID_HARDDISK, ID_HARDDISK);
			SetButtonState(hItem, strDrive);
			if(bFirst)
			{
				GetTreeCtrl().SelectItem(hItem);
				GetTreeCtrl().Expand(hItem, TVE_EXPAND);
				bFirst = FALSE;
			}
			break;
		// 네트워크 드라이브인 경우
		case DRIVE_REMOTE:
			hItem = GetTreeCtrl().InsertItem(strDrive, ID_NETDRIVE, ID_NETDRIVE);
			SetButtonState(hItem, strDrive);
			break;
		// CD 롬 드라이브인 경우
		case DRIVE_CDROM:
			hItem = GetTreeCtrl().InsertItem(strDrive, ID_CDROM, ID_CDROM);
			GetTreeCtrl().InsertItem("", ID_CLOSEDFOLDER, ID_CLOSEDFOLDER, hItem);
			break;
		// RAM 드라이브인 경우
		case DRIVE_RAMDISK:
			hItem = GetTreeCtrl().InsertItem(strDrive, ID_HARDDISK, ID_HARDDISK);
			SetButtonState(hItem, strDrive);
			break;
		default:
			return FALSE;
	}
	return TRUE;
}


// 서브 디렉토리 존재 여부 확인
BOOL CDriveView::SetButtonState(HTREEITEM hItem, CString strPath)
{
	if(strPath.Right(1) != "\\") strPath += "\\";
	strPath += "*.*";
	
	CString strDirName;
	CFileFind filefind;
	BOOL bContinue;
	if(!(bContinue = filefind.FindFile(strPath)))
		return FALSE;

	// 지정된 디렉토리의 파일을 차례로 읽어옴
	while(bContinue)
	{
		bContinue = filefind.FindNextFile();
		// 디렉토리이면...
		if(filefind.IsDirectory())
		{
			strDirName = filefind.GetFileName();
			if((strDirName != ".") && (strDirName != ".."))
			{
				// "."나 ".."가 아닌 디렉토리이면 더미노드를 하나 추가하고 빠져 나옴
				GetTreeCtrl().InsertItem("", ID_CLOSEDFOLDER, ID_CLOSEDFOLDER, hItem);
				break;
			}
		}
	}
	filefind.Close();
	return TRUE;
}

// 노드의 확장, 축소를 알리는 트리 컨트롤의 통시 메시지 처리
void CDriveView::OnItemexpanding(NMHDR* pNMHDR, LRESULT* pResult) 
{
	NM_TREEVIEW* pNMTreeView = (NM_TREEVIEW*)pNMHDR;

	// 확장되거나 축소된 노드를 얻음
	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	// 확장되거나 축소된 노드가 나타내는 디렉토리명을 얻음
	CString str = GetPathFromNode(hItem);

	*pResult = FALSE;

	// 노드가 확장되는 경우
	if(pNMTreeView->action == TVE_EXPAND)
	{
		// 앞서 추가했던 더미 노드 제거
		DeleteFirstChild(hItem);
		// 진짜 디렉토리 구조를 읽어 표시
		if(AddDir(hItem, str) == 0)
			*pResult = TRUE;
	}
	else	// 노드가 축소되는 경우
	{
		// 모든 하위 노드를 제거
		DeleteAllChildren(hItem);
		// 십자가 모양의 버튼 표시를 위해 더미 노드 추가
		if(GetTreeCtrl().GetParentItem(hItem) == NULL)
			GetTreeCtrl().InsertItem("", ID_CLOSEDFOLDER, ID_CLOSEDFOLDER, hItem);
		else	// 서브 디렉토리 존재 유무 확인
			SetButtonState(hItem, str);
	}
}

//주어진 노드가 나타내는 디렉토리명 얻기
CString CDriveView::GetPathFromNode(HTREEITEM hItem)
{
	CString strResult = GetTreeCtrl().GetItemText(hItem);

	HTREEITEM hParent;
	// 루트 노드를 만날 때까지 진행...
	while((hParent = GetTreeCtrl().GetParentItem(hItem)) != NULL)
	{
		CString str = GetTreeCtrl().GetItemText(hParent);
		if(str.Right(1) != "\\")
			str += "\\";
		strResult = str + strResult;
		hItem = hParent;
	}
	return strResult;
}

// 하나의 자식 노드 제거하기
void CDriveView::DeleteFirstChild(HTREEITEM hParent)
{
	HTREEITEM hItem;
	if((hItem = GetTreeCtrl().GetChildItem(hParent)) != NULL)
		GetTreeCtrl().DeleteItem(hItem);
}

// 모든 자식 노드 제거하기
void CDriveView::DeleteAllChildren(HTREEITEM hParent)
{
	HTREEITEM hItem;
	if((hItem = GetTreeCtrl().GetChildItem(hParent)) == NULL)
		return;

	do
	{
		HTREEITEM hNextItem = GetTreeCtrl().GetNextSiblingItem(hItem);
		GetTreeCtrl().DeleteItem(hItem);
		hItem = hNextItem;
	} while (hItem != NULL);
}


int CDriveView::AddDir(HTREEITEM hItem, CString &strPath)
{
	CString strFindPath;
	HTREEITEM hNewItem;
	int nCount = 0;

	// 지정된 디렉토리 뒤에 "\\*.*"를 덧붙임
	// 예: strPath = "C:\\Temp"이면, strFindPath = "C:\\Temp\\*.*"가 됨
	strFindPath = strPath;
	if(strFindPath.Right(1) != "\\") strFindPath += "\\";
	strFindPath += "*.*";

	CString strFileName, strNewPath; 
	CFileFind filefind;

	// 탐색할 디렉토리 지정
	BOOL bContinue;
	if(!(bContinue = filefind.FindFile(strFindPath)))
	{
		if(GetTreeCtrl().GetParentItem(hItem) == NULL)
			GetTreeCtrl().InsertItem("", ID_CLOSEDFOLDER, ID_CLOSEDFOLDER, hItem);
		return 0;
	}

	// 디렉토리 탐색
	while(bContinue)
	{
		bContinue = filefind.FindNextFile();
		// 디렉토리인 경우…
		if(filefind.IsDirectory()) 
		{
			strFileName = filefind.GetFileName();
			if(strFileName != "." && strFileName != "..")
			{
				// 노드 추가
				hNewItem = GetTreeCtrl().InsertItem((LPCTSTR)strFileName, ID_CLOSEDFOLDER, ID_OPENFOLDER, hItem);
				
				strNewPath = strPath;
				if(strNewPath.Right(1) != "\\")	strNewPath += "\\";
				
				// 추가된 노드가 서브 디렉토리를 가졌는지 확인
				strNewPath += strFileName;
				SetButtonState(hNewItem, strNewPath);
				nCount++;
			}
		}
	}

	filefind.Close();
	return nCount;
}


// 선택된 디렉토리 변경 여부를 리스트뷰에 알리기
void CDriveView::OnSelchanged(NMHDR* pNMHDR, LRESULT* pResult) 
{
	NM_TREEVIEW* pNMTreeView = (NM_TREEVIEW*)pNMHDR;

	CString strPath = GetPathFromNode(pNMTreeView->itemNew.hItem);
	GetDocument()->UpdateAllViews(this, (LPARAM)(LPCTSTR)strPath);
}


