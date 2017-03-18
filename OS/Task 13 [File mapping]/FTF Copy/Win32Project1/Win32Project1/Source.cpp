#include <Windows.h>
#include "resource.h"
#include <windowsx.h>

#define MAX_LOADSTRING 100

#define RHANDLE_MSG(hwnd, message, fn)    \
    case (message): return RHANDLE_##message((hwnd), (wParam), (lParam), (fn))

#define RHANDLE_WM_COMMAND(hwnd, wParam, lParam, fn) \
    ((fn)((hwnd), (int)(LOWORD(wParam)), (HWND)(lParam), (UINT)HIWORD(wParam)), -1L)

#define RHANDLE_WM_INITDIALOG(hwnd, wParam, lParam, fn) \
    (LRESULT)(DWORD)(UINT)(BOOL)(fn)((hwnd), (HWND)(wParam), lParam)

#define RFORWARD_WM_COMMAND(hwnd, id, hwndCtl, codeNotify, fn) \
    (LONG)(fn)((hwnd), WM_COMMAND, MAKEWPARAM((UINT)(id),(UINT)(codeNotify)), (LPARAM)(HWND)(hwndCtl))


TCHAR szMutexName[] = "$MutexFor3Threads$";
HANDLE hMutex;

TCHAR szTitle[MAX_LOADSTRING];
TCHAR szWindowClass[MAX_LOADSTRING];
HINSTANCE hInst;

TCHAR szKey[MAX_LOADSTRING];
UINT  uLengthKey;
TCHAR szSourceFilePath[260];
TCHAR szDestinationFilePath[260];
TCHAR szSourceDirName[260];

TCHAR szFileTitle[260];
TCHAR szFilter[260] = TEXT("Text Files\0*.txt\0Encripted Files\0*.crpt\0Any Files\0*.*\0\0");
TCHAR szDlgTitleOpen[260] = TEXT("Select source file");
TCHAR szDlgTitleSave[260] = TEXT("Select destination file");

BOOL bDirection;

OPENFILENAME ofn;

UINT uLenNewKey;
TCHAR szNewKey[260];

ATOM MyRegisterClass(HINSTANCE hInstance);
BOOL InitInstance(HINSTANCE, int);
LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);
LONG WndProc_OnCreate(HWND hWnd, LPCREATESTRUCT lpCreateStruct);
void WndProc_OnDestroy(HWND hWnd);
void WndProc_OnPaint(HWND hWnd);
LONG WndProc_OnCommand(HWND hWnd, int id, HWND hwndCtl, UINT codeNotify);
void WndProc_OnClose(HWND hWnd);
LRESULT CALLBACK ExitBox(HWND, UINT, WPARAM, LPARAM);
BOOL GetFileOpen(HWND hWnd, LPOPENFILENAME lpofn);
BOOL GetFileEncripted(HWND hWnd, LPOPENFILENAME lpofn);
LRESULT CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK OptionsBox(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam);
LONG OptionsBox_OnCommand(HWND hDlg, int id, HWND hwndCtl, UINT codeNotify);
BOOL OptionsBox_OnInitDialog(HWND hDlg, HWND hwndFocus, LPARAM lParam);
BOOL EncriptFileToFile(LPSTR lpstrSourceFile, LPSTR lpstrDestinationFile);
bool CopyFileToFile(HANDLE hSourceFile, HANDLE hDestFile);
bool Simplest(unsigned uLengthKey, LPSTR szKey, HANDLE hDestFile);


int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow)
{
	//Create an object Mutex (named)
	//As it is named it is possible to verify if there is only one application instance!

	if (!(hMutex = CreateMutex(NULL, FALSE, szMutexName))) {//FALSE sets it In SIGNALED STATE!
		MessageBox(NULL, "CreateMutex Error", "Four Threads", MB_OK | MB_ICONEXCLAMATION);
		return 1;
	}

	if (GetLastError() == ERROR_ALREADY_EXISTS) 
	{
		MessageBox(NULL, "Mutex already started", "Four Threads", MB_OK | MB_ICONEXCLAMATION);
		return 1;
	}

	MSG msg;
	HACCEL hAccelTable;

	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_FILETOFILE, szWindowClass, MAX_LOADSTRING);
	MyRegisterClass(hInstance);

	if (!InitInstance(hInstance, nCmdShow))return FALSE;
	hAccelTable = LoadAccelerators(hInstance, (LPCTSTR)IDC_FILETOFILE);

	while (GetMessage(&msg, NULL, 0, 0))
	{
		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	return msg.wParam;
}

ATOM MyRegisterClass(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;

	wcex.cbSize = sizeof(WNDCLASSEX);

	wcex.style = CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc = (WNDPROC)WndProc;
	wcex.cbClsExtra = 0;
	wcex.cbWndExtra = 0;
	wcex.hInstance = hInstance;
	wcex.hIcon = LoadIcon(hInstance, (LPCTSTR)IDC_FILETOFILE);
	wcex.hCursor = LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wcex.lpszMenuName = (LPCSTR)IDC_FILETOFILE;
	wcex.lpszClassName = szWindowClass;
	wcex.hIconSm = LoadIcon(wcex.hInstance, (LPCTSTR)IDI_SMALL);

	return RegisterClassEx(&wcex);
}



BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
	HWND hWnd;
	hInst = hInstance;
	hWnd = CreateWindow(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);
	if (!hWnd)return FALSE;

	ShowWindow(hWnd, nCmdShow);
	UpdateWindow(hWnd);

	return TRUE;
}

LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	//	int wmId, wmEvent;
	switch (message)
	{
		HANDLE_MSG(hWnd, WM_CREATE, WndProc_OnCreate);//
		HANDLE_MSG(hWnd, WM_COMMAND, WndProc_OnCommand);//!!
		HANDLE_MSG(hWnd, WM_PAINT, WndProc_OnPaint);//!!!!
		HANDLE_MSG(hWnd, WM_DESTROY, WndProc_OnDestroy);//!!
		HANDLE_MSG(hWnd, WM_CLOSE, WndProc_OnClose);//!
	default:return DefWindowProc(hWnd, message, wParam, lParam);
	}
}

LRESULT CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch (message)
	{
	case WM_INITDIALOG:
		return TRUE;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
		{
			EndDialog(hDlg, LOWORD(wParam));
			return TRUE;
		}
		break;
	}
	return FALSE;
}

LONG WndProc_OnCreate(HWND hWnd, LPCREATESTRUCT lpCreateStruct)
{
	bDirection = TRUE;
	LoadString(hInst, IDS_APP_TITLE, szKey, MAX_LOADSTRING);
	uLengthKey = strlen(szKey);

	szSourceFilePath[0] = CHAR('\0');
	szDestinationFilePath[0] = CHAR('\0');
	return TRUE;//BOOL
}

void WndProc_OnDestroy(HWND hWnd)
{
	PostQuitMessage(0);
}

void WndProc_OnClose(HWND hWnd)
{
	DialogBox(hInst, (LPCTSTR)IDD_ONCLOSE, hWnd, (DLGPROC)ExitBox);
	PostQuitMessage(0);
}

LRESULT CALLBACK ExitBox(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch (message)
	{
	case WM_INITDIALOG:
		return TRUE;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDC_BUTTONEXIT)
		{
			EndDialog(hDlg, LOWORD(wParam));
			return TRUE;
		}
		break;
	}
	return FALSE;
}

void WndProc_OnPaint(HWND hWnd)
{
	PAINTSTRUCT ps;
	HDC hdc;
	TCHAR szHello[MAX_LOADSTRING];
	LoadString(hInst, IDS_HELLO, szHello, MAX_LOADSTRING);
	hdc = BeginPaint(hWnd, &ps);
	RECT rt;
	GetClientRect(hWnd, &rt);
	DrawText(hdc, szHello, strlen(szHello), &rt, DT_CENTER);
	EndPaint(hWnd, &ps);
}


LONG WndProc_OnCommand(HWND hWnd, int id, HWND hwndCtl, UINT codeNotify)
{
	// Parse the menu selections:
	switch (id)
	{
	case IDM_OPENFILE:
		//Get File Name and File Type
		if (GetFileOpen(hWnd, &ofn))MessageBox(NULL, ofn.lpstrFile, "Get File Open", MB_OK);
		return 0;
	case IDM_ENCRIPT:
		//Call Encript Function
		if (GetFileEncripted(hWnd, &ofn))//TODO!!!
		{
			MessageBox(NULL, ofn.lpstrFile, "Encripted File", MB_OK);
		}
		else
			MessageBox(NULL, ofn.lpstrFile, "Not Encripted File", MB_OK);
		return 0L;
		break;
	case IDM_OPTIONS:	//Done!
		DialogBox(hInst, (LPCTSTR)IDD_DIALOG_OPTIONS, hWnd, (DLGPROC)OptionsBox);
		return 0L;
		break;

	case IDM_ABOUT:		//Done!
		DialogBox(hInst, (LPCTSTR)IDD_ABOUTBOX, hWnd, (DLGPROC)About);
		return 0L;
		break;
	case IDM_EXIT:		//Done!
		PostQuitMessage(0);
		return 0L;
		//?DestroyWindow(hWnd);					
		break;
	default:break;
		// return DefWindowProc(hWnd, message, wParam, lParam);
	}
	return RFORWARD_WM_COMMAND(hWnd, id, hwndCtl, codeNotify, DefWindowProc);
}

BOOL GetFileOpen(HWND hWnd, LPOPENFILENAME lpofn)
{
	memset(lpofn, 0, sizeof(OPENFILENAME));
	GetCurrentDirectory(sizeof(szSourceDirName), szSourceDirName);
	szSourceFilePath[0] = TCHAR('\0');

	lpofn->lStructSize = sizeof(OPENFILENAME);
	lpofn->hwndOwner = hWnd;

	lpofn->lpstrFilter = szFilter;
	lpofn->nFilterIndex = 1;

	lpofn->lpstrInitialDir = szSourceDirName;
	lpofn->lpstrFile = szSourceFilePath;
	lpofn->nMaxFile = sizeof(szSourceFilePath);

	lpofn->lpstrFileTitle = szFileTitle;
	lpofn->nMaxFileTitle = sizeof(szFileTitle);
	lpofn->lpstrTitle = szDlgTitleOpen;

	lpofn->Flags = OFN_PATHMUSTEXIST | OFN_FILEMUSTEXIST | OFN_HIDEREADONLY;
	if (GetOpenFileName(lpofn))
	{
		//File chosen
		return TRUE;
	}
	//File was not chosen
	return FALSE;
}


LRESULT CALLBACK OptionsBox(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch (message)
	{
		RHANDLE_MSG(hDlg, WM_INITDIALOG, OptionsBox_OnInitDialog);//!!
		RHANDLE_MSG(hDlg, WM_COMMAND, OptionsBox_OnCommand);//!!
															//return true; for all handled messages
	default:
		return FALSE;//not handled messages		 
	}
}//OptionsBox   

LONG OptionsBox_OnCommand(HWND hDlg, int id, HWND hwndCtl, UINT codeNotify)
{
	// Parse the Options Box commands:
	switch (id)
	{
	case IDOK:

		bDirection = IsDlgButtonChecked(hDlg, IDC_RADIO_DIRECT) ? TRUE : FALSE;
		strcpy(szKey, szNewKey);
		uLengthKey = uLenNewKey;

	case IDCANCEL:
		EndDialog(hDlg, id);
		return TRUE;//handled

	case IDC_EDIT_KEY:
		if (codeNotify == EN_CHANGE)uLenNewKey = GetDlgItemText(hDlg, IDC_EDIT_KEY, szNewKey, MAX_LOADSTRING);
		return TRUE;
	default:
		return TRUE;
	}//return TRUE for all handled commands,any others are not here
}//OptionsBox_OnCommand

BOOL OptionsBox_OnInitDialog(HWND hDlg, HWND hwndFocus, LPARAM lParam)
{
	//Set Current Direction and Key Values

	CheckDlgButton(hDlg, IDC_RADIO_DIRECT, bDirection);
	CheckDlgButton(hDlg, IDC_RADIO_BACKWARD, !bDirection);

	SendDlgItemMessage(hDlg, IDC_EDIT_KEY, WM_SETTEXT, 0, (LPARAM)szKey);//EM_REPLACESEL

	return TRUE;//handled
}//OptionsBox_OnInitDialog

BOOL GetFileEncripted(HWND hWnd, LPOPENFILENAME lpofn)
{
	if (strlen(szSourceFilePath) == 0)
	{
		MessageBox(NULL, TEXT("Select a source file name!"), TEXT("Encript File"), MB_OK);
		return FALSE;
	}

	lpofn->lpstrTitle = szDlgTitleSave;
	lpofn->Flags = OFN_HIDEREADONLY;

	lpofn->lpstrFile = szDestinationFilePath;

	if (GetSaveFileName(lpofn))
	{
		if (0 == strcmp(szDestinationFilePath, szSourceFilePath))
		{
			MessageBox(NULL, TEXT("Select different file names!"), TEXT("Encript File"), MB_OK);
			return FALSE;
		}
		return EncriptFileToFile(szSourceFilePath, szDestinationFilePath);
	}
	return FALSE;
}/////GetFileEncripted

BOOL EncriptFileToFile(LPSTR lpstrSourceFile, LPSTR lpstrDestinationFile)
{
	TCHAR message[260];

//	wsprintf(message, "Source: %s\nDestination: %s\nDirection: %s\nKey: %s\n",lpstrSourceFile,lpstrDestinationFile,bDirection ? "Direct" : "Backward",szKey);
//	MessageBox(NULL, message, TEXT("Encript File To File"), MB_OK);

	//-----------------------------------------
	//Create Handles to Source and Destination Files 
	//-----------------------------------------
	HANDLE hSourceFile, hDestFile;

	//Create Handle to Source File 
	hSourceFile = CreateFile(lpstrSourceFile,GENERIC_READ,FILE_SHARE_READ,NULL,OPEN_EXISTING,FILE_ATTRIBUTE_READONLY | FILE_FLAG_SEQUENTIAL_SCAN,NULL);

	if (hSourceFile == INVALID_HANDLE_VALUE)
	{
		wsprintf(message, "hSourceFile is INVALID_HANDLE_VALUE; ERROR %ld\n", GetLastError());
		MessageBox(NULL, message, TEXT("Encript File To File"), MB_OK);
		return FALSE;
	}
	//hSourceFile has just created!
	//--------------------------------------------
	//Create Handle to Destination File 
	hDestFile = CreateFile(lpstrDestinationFile,GENERIC_WRITE,0,NULL,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,NULL);

	if (hDestFile == INVALID_HANDLE_VALUE)
	{
		wsprintf(message, "1)hDestFile is INVALID_HANDLE_VALUE; ERROR %ld\n", GetLastError());
		MessageBox(NULL, message, TEXT("Encript File To File"), MB_OK);
		CloseHandle(hSourceFile);
		return FALSE;
	}
	//hDestFile has just created!
	//-------------------------------------------- 
	if (strlen(szKey) != uLengthKey)
	{
		MessageBox(NULL, TEXT("strlen(szKey)!=uLengthKey"), TEXT("Encript File To File"), MB_OK);
		CloseHandle(hSourceFile);
		CloseHandle(hDestFile);
		return false;
	}

	if (!CopyFileToFile(hSourceFile, hDestFile))
	{
		MessageBox(NULL, TEXT("CopyFileToFile failed... "), TEXT("Encript File To File"), MB_OK);
		CloseHandle(hSourceFile);
		CloseHandle(hDestFile);
		return false;
	}
	CloseHandle(hSourceFile);
	CloseHandle(hDestFile);

	//##################################################//
	//Create Handle to Destination File to be mapped onto memory

	hDestFile = CreateFile(lpstrDestinationFile, GENERIC_READ | GENERIC_WRITE,0,NULL,OPEN_EXISTING,0,NULL);

	if (hDestFile == INVALID_HANDLE_VALUE)
	{
		wsprintf(message, "2)hDestFile is INVALID_HANDLE_VALUE; ERROR %ld\n", GetLastError());
		MessageBox(NULL, message, TEXT("Encript File To File"), MB_OK);
		return FALSE;
	}

	//2)hDestFile has just created!
	//-------------------------------------------- 	
	bool res = Simplest(uLengthKey, szKey, hDestFile);

	CloseHandle(hDestFile);
	return res;

}/////EncriptFileToFile

bool CopyFileToFile(HANDLE hSourceFile, HANDLE hDestFile) {

	TCHAR  inBuf[512];
	DWORD  nBytesRead, nBytesWritten;
	BOOL   bResult;

	while (true)
	{
		bResult = ReadFile(hSourceFile, inBuf, 512, &nBytesRead, NULL);//Offset not used
		if (bResult && nBytesRead == 0)
			break;//End of File
		WriteFile(hDestFile, inBuf, nBytesRead, &nBytesWritten, NULL);
	}
	return true;
}//CopyFileToFile

bool Simplest(unsigned uLengthKey, LPSTR szKey, HANDLE hDestFile)
{
	DWORD dwFileSize;
	HANDLE hFileMapping;
	LPVOID lpFileMap;
	SYSTEM_INFO  SystemInfo;

	//---Get a File Size of	hDestFile

	dwFileSize = GetFileSize(hDestFile,
		NULL);// pointer to high-order word for file size
			  //--Get System Allocation  Granularity
	GetSystemInfo(&SystemInfo);  // address of system information structure

	TCHAR szGranularity[200];
	wsprintf(szGranularity, "Gr=%ld", SystemInfo.dwAllocationGranularity);
	MessageBox(NULL, szGranularity, "Granularity", MB_OK);

	//----Specify "view window"-------------------------------------------//

	DWORD dwViewWindowSize = SystemInfo.dwAllocationGranularity;//or some times of that

																//----Specify how many "view windows" cover the file hDestFile--------//

	DWORD dwNumberOfWindows = dwFileSize / dwViewWindowSize;
	DWORD dwResidue = dwFileSize % dwViewWindowSize;

	unsigned k, i;

	//------The whole file is to be processed--------//
	DWORD dwMaximumSizeLow = dwFileSize;
	//-----------------------------------------------//
	hFileMapping = CreateFileMapping(hDestFile,
		NULL,
		//PAGE_WRITECOPY,
		PAGE_READWRITE,	//The file specified by hFile must have been created
						//with GENERIC_READ and GENERIC_WRITE access.
		0,
		dwMaximumSizeLow,	//Specifies the low-order 32 bits of the maximum size 
							//of the file-mapping object.
							//If this parameter and dwMaximumSizeHigh are zero,
							// the maximum size of the file-mapping object is equal to 
							//the current size of the file identified by hFile
		NULL);			//the name of the mapping object. 

	if (hFileMapping == NULL)		return false;



	//----------Loop on dwNumberOfWindows---------------------------------//
	for (i = 0; i < dwNumberOfWindows; i++)
	{


		lpFileMap = MapViewOfFile(hFileMapping,
			FILE_MAP_WRITE, //The hFileMappingObject parameter 
							//must have been created with PAGE_READWRITE protection. 
							//A read-write view of the file is mapped.
			0,
			(DWORD)(i*dwViewWindowSize),	//an offset within the file
			dwViewWindowSize);				//Specifies the number of bytes of the file to map.
											//If dwNumberOfBytesToMap is zero, the entire file is mapped. 
		if (NULL == lpFileMap) {
			TCHAR mess[200];
			wsprintf(mess, "NULL == lpFileMap Error:%ld", GetLastError());
			MessageBox(NULL, mess, "1)MapViewOfFile", MB_OK);
			CloseHandle(hFileMapping);
			return false;
		}

		//|Encript

		//---------Encript "view window" under a number i ------------//	 
		for (k = 0; k < dwViewWindowSize; k++)
			((char*)lpFileMap)[k] = ((char*)lpFileMap)[k] ^ szKey[0];

		//------All or some "view windows" can be flushed-------------//
		//	  FlushViewOfFile(  lpFileMap,  // start address of byte range to flush
		//						dwViewWindowSize );// number of bytes in range 


		UnmapViewOfFile(lpFileMap);


	}//--continue for-loop

	 //------Encript a remainder of the file hDestFile-----//

	if (!dwResidue) {
		CloseHandle(hFileMapping);	return true;
	}



	lpFileMap = MapViewOfFile(hFileMapping,

		//FILE_MAP_COPY,
		FILE_MAP_WRITE, //The hFileMappingObject parameter 
						//must have been created with PAGE_READWRITE protection. 
						//A read-write view of the file is mapped.
		0,
		dwFileSize - dwResidue,	//an offset within the file
		dwResidue);				//Specifies the number of bytes of the file to map.

	if (NULL == lpFileMap) {
		TCHAR mess[200];
		wsprintf(mess, "NULL == lpFileMap Error:%ld", GetLastError());
		MessageBox(NULL, mess, "2)MapViewOfFile", MB_OK);
		CloseHandle(hFileMapping);
		return false;
	}
	//-----Encript the remainder of the file hDestFile-

	int id = 0;
	for (k = 0; k < dwResidue; k++) 
	{//id -> full key, 0 - simple key
		((char*)lpFileMap)[k] = ((char*)lpFileMap)[k] ^ szKey[id++];
		if (id == uLengthKey)id = 0;
	}

	UnmapViewOfFile(lpFileMap);

	CloseHandle(hFileMapping);	return true;

}//Simplest