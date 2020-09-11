// Explorer.h : main header file for the EXPLORER application
//

#if !defined(AFX_EXPLORER_H__39586AA5_2906_11D2_8F72_006097283F10__INCLUDED_)
#define AFX_EXPLORER_H__39586AA5_2906_11D2_8F72_006097283F10__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"       // main symbols

/////////////////////////////////////////////////////////////////////////////
// CExplorerApp:
// See Explorer.cpp for the implementation of this class
//

class CExplorerApp : public CWinApp
{
public:
	CExplorerApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CExplorerApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation
	//{{AFX_MSG(CExplorerApp)
	afx_msg void OnAppAbout();
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_EXPLORER_H__39586AA5_2906_11D2_8F72_006097283F10__INCLUDED_)
