// FileView.h : interface of the CFileView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_FILEVIEW_H__39586AAD_2906_11D2_8F72_006097283F10__INCLUDED_)
#define AFX_FILEVIEW_H__39586AAD_2906_11D2_8F72_006097283F10__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

typedef struct tagFILEITEM
{
	CString		strFileName;
	CString		strFilePath;
	DWORD		nFileSize;
	CTime		timeLastWriteTime;
} FILEITEM;

class CFileView : public CListView
{
protected: // create from serialization only
	CFileView();
	DECLARE_DYNCREATE(CFileView)

// Attributes
public:
	CExplorerDoc* GetDocument();
	int sortOps;

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CFileView)
	public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual void OnInitialUpdate(); // called first time after construct
	virtual void OnUpdate(CView* pSender, LPARAM lHint, CObject* pHint);
	//}}AFX_VIRTUAL

// Implementation
public:
	CImageList m_imgSmallList, m_imgLargeList;
	void GetSystemImageList();
	int GetIconIndex(CString FileName);
	void FreeItemMemory();
	void InsertColumn();
	BOOL AddItem(int nIndex, CFileFind *pFileFind);
	int SetListData(LPCTSTR pszPath);
	void SwitchSortOps();
	virtual ~CFileView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CFileView)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnGetdispinfo(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg void OnDestroy();
	afx_msg void OnColumnclick(NMHDR* pNMHDR, LRESULT* pResult);
	//}}AFX_MSG
	afx_msg void OnStyleChanged(int nStyleType, LPSTYLESTRUCT lpStyleStruct);
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnEditCut();
	afx_msg void OnEditCopy();
	afx_msg void OnEditPaste();
	CString copyPath;
	CString copyName;

	afx_msg void OnNMDblclk(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnContextMenu(CWnd* /*pWnd*/, CPoint point);
	FILEITEM* GetSelectItem(CString cPath);
};

#ifndef _DEBUG  // debug version in FileView.cpp
inline CExplorerDoc* CFileView::GetDocument()
   { return (CExplorerDoc*)m_pDocument; }
#endif



int CALLBACK CompareFunc(LPARAM lParam1, LPARAM lParam2, LPARAM lParamSort);
/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_FILEVIEW_H__39586AAD_2906_11D2_8F72_006097283F10__INCLUDED_)
