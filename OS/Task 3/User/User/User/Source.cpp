#include <Windows.h>
#include <cstdlib>
#include "resource.h"

using namespace std;

#define STRING1MENU 10001
#define STRING2MENU 10002
#define STRING3MENU 10003

LRESULT CALLBACK WindowProc(HWND, UINT, WPARAM, LPARAM);

HMODULE hResourceLib = NULL;

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE, LPSTR, int ss) {
	hResourceLib = LoadLibraryEx(L"ResourcesDLL.dll", NULL, LOAD_LIBRARY_AS_DATAFILE);


	WNDCLASS wc;
	wc.style = 0;
	wc.lpfnWndProc = WindowProc;
	wc.cbClsExtra = wc.cbWndExtra = 0;
	wc.hInstance = hInstance;
	wc.hIcon = NULL;
	wc.hCursor = NULL;
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wc.lpszMenuName = NULL;
	wc.lpszClassName = L"WindowClass";
	if (!RegisterClass(&wc)) return FALSE;

	HMENU mainMenu = CreateMenu();
	AppendMenu(mainMenu, MF_STRING, STRING1MENU, L"Show 1st string");
	AppendMenu(mainMenu, MF_STRING, STRING2MENU, L"Show 2nd string");
	AppendMenu(mainMenu, MF_STRING, STRING3MENU, L"Show 3rd string");

	HWND window = CreateWindow(L"WindowClass", L"WINAPI Lab3 App", WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, NULL, NULL, hInstance, NULL);
	if (!window) return FALSE;
	
	SetMenu(window, mainMenu);

	ShowWindow(window, ss);
	UpdateWindow(window);


	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0)) {
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}
	FreeLibrary(hResourceLib);
	return msg.wParam;
}

LRESULT CALLBACK WindowProc(HWND hw, UINT msg, WPARAM wp, LPARAM lp) {
	switch (msg) {
		case WM_CREATE:
			break;
		case WM_COMMAND:
			if (HIWORD(wp) != 0) return 0;
			switch (LOWORD(wp)) {
				TCHAR p[160];
				case STRING1MENU:
					LoadString(hResourceLib, STRING1, p, sizeof(p) / sizeof(TCHAR));
					MessageBox(NULL, p, L"Caption", 0);
					break;
				case STRING2MENU:
					LoadString(hResourceLib, STRING2, p, sizeof(p) / sizeof(TCHAR));
					MessageBox(NULL, p, L"Caption", 0);
					break;
				case STRING3MENU:
					LoadString(hResourceLib, STRING3, p, sizeof(p) / sizeof(TCHAR));
					MessageBox(NULL, p, L"Caption", 0);
					break;
				default:
					break;
			}
			break;
		case WM_DESTROY:
			PostQuitMessage(0);
			break;
		default:
			return DefWindowProc(hw, msg, wp, lp);
	}
	return 0;
}