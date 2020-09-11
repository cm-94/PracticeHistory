// DriverView.h : interface of the CDriveView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_DRIVERVIEW_H__39586AAF_2906_11D2_8F72_006097283F10__INCLUDED_)
#define AFX_DRIVERVIEW_H__39586AAF_2906_11D2_8F72_006097283F10__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CExplorerDoc;

#define ID_FLOPPY		0
#define ID_HARDDISK		1
#define ID_CDROM		2
#define ID_NETDRIVE		3
#define ID_CLOSEDFOLDER	4
#define ID_OPENFOLDER	5

class CDriveView : public CTreeView
{
protected: // create from serialization only
	CDriveView();
	DECLARE_DYNCREATE(CDriveView)

// Attributes
public:
	CExplorerDoc* GetDocument();

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDriveView)
	public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual void OnInitialUpdate(); // called first time after construct
	//}}AFX_VIRTUAL

// Implementation
public:
	void DeleteAllChildren(HTREEITEM hParent);
	void DeleteFirstChild(HTREEITEM hParent);
	CImageList m_imgDrives;
	int AddDir(HTREEITEM hItem, CString &strPath);
	CString GetPathFromNode(HTREEITEM hItem);
	BOOL SetButtonState(HTREEITEM hItem, CString strPath);
	BOOL AddDriveNode(CString &strDrive);
	int	 InitDriveView();
	virtual ~CDriveView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CDriveView)
	afx_msg void OnItemexpanding(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg void OnSelchanged(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in DriverView.cpp
inline CExplorerDoc* CDriveView::GetDocument()
   { return (CExplorerDoc*)m_pDocument; }
#endif

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DRIVERVIEW_H__39586AAF_2906_11D2_8F72_006097283F10__INCLUDED_)
