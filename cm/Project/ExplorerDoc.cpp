// ExplorerDoc.cpp : implementation of the CExplorerDoc class
//

#include "stdafx.h"
#include "Explorer.h"

#include "ExplorerDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CExplorerDoc

IMPLEMENT_DYNCREATE(CExplorerDoc, CDocument)

BEGIN_MESSAGE_MAP(CExplorerDoc, CDocument)
	//{{AFX_MSG_MAP(CExplorerDoc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CExplorerDoc construction/destruction

CExplorerDoc::CExplorerDoc()
{
	// TODO: add one-time construction code here

}

CExplorerDoc::~CExplorerDoc()
{
}

BOOL CExplorerDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}



/////////////////////////////////////////////////////////////////////////////
// CExplorerDoc serialization

void CExplorerDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}

/////////////////////////////////////////////////////////////////////////////
// CExplorerDoc diagnostics

#ifdef _DEBUG
void CExplorerDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CExplorerDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CExplorerDoc commands
