// ExplorerDoc.h : interface of the CExplorerDoc class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_EXPLORERDOC_H__39586AAB_2906_11D2_8F72_006097283F10__INCLUDED_)
#define AFX_EXPLORERDOC_H__39586AAB_2906_11D2_8F72_006097283F10__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CExplorerDoc : public CDocument
{
protected: // create from serialization only
	CExplorerDoc();
	DECLARE_DYNCREATE(CExplorerDoc)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CExplorerDoc)
	public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CExplorerDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CExplorerDoc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_EXPLORERDOC_H__39586AAB_2906_11D2_8F72_006097283F10__INCLUDED_)
