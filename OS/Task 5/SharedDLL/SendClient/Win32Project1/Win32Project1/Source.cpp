#include "SharedBufferDLL.h"

#define APPLY_TEXT 10001

LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);

HWND hEdit;

UINT msgBufferUpdate = INVALID_ATOM;

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPWSTR lpCmdLine, int nCmdShow) {
	WNDCLASSEX wc;

	msgBufferUpdate = RegisterWindowMessage(TEXT("MsgSendClientUpdatedBuffer"));//systemwide message to notify another instances

	static TCHAR szWindowClass[] = L"WndClass";

	wc.cbSize = sizeof(WNDCLASSEX);
	wc.style = CS_HREDRAW | CS_VREDRAW;
	wc.lpfnWndProc = WndProc;
	wc.cbClsExtra = 0;
	wc.cbWndExtra = 0;
	wc.hInstance = hInstance;
	wc.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_APPLICATION));
	wc.hCursor = LoadCursor(NULL, IDC_ARROW);
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wc.lpszMenuName = NULL;
	wc.lpszClassName = szWindowClass;
	wc.hIconSm = LoadIcon(wc.hInstance, MAKEINTRESOURCE(IDI_APPLICATION));

	if (!RegisterClassEx(&wc))
	{
		MessageBox(NULL, L"RegisterClassEx failed.", L"Error", NULL);
		return 1;
	}

	HMENU mainMenu = CreateMenu();
	AppendMenu(mainMenu, MF_STRING, APPLY_TEXT, L"Set text to buffer");

	HWND hWnd = CreateWindow(szWindowClass,L"SharedBufferDLL SendClient",WS_OVERLAPPEDWINDOW,CW_USEDEFAULT, CW_USEDEFAULT,500, 500,NULL,mainMenu,hInstance,NULL);

	if (!hWnd)
	{
		MessageBox(NULL,L"CreateWindow failed!",L"Error",NULL);
		return 1;
	}

	hEdit = CreateWindowEx(WS_EX_CLIENTEDGE, L"EDIT", L"", WS_CHILD | WS_VISIBLE | ES_AUTOHSCROLL, 10, 10, 100, 20, hWnd, NULL, GetModuleHandle(NULL), NULL);
	

	ShowWindow(hWnd,nCmdShow);
	UpdateWindow(hWnd);

	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	return (int)msg.wParam;
}



LRESULT CALLBACK WndProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	switch (msg)
	{
	case WM_CREATE:
		
		break;
	case WM_COMMAND:
		switch (LOWORD(wParam)) {
		case APPLY_TEXT:
			bIsMessageInBuffer = true;
			GetWindowText(hEdit, wcSharedBufferDLL, 500);
			PostMessage(HWND_BROADCAST, msgBufferUpdate, 0, 0);
			break;
		default:
			break;
		}
		break;
	case WM_PAINT:
	{
		PAINTSTRUCT ps;
		HDC hdc = BeginPaint(hWnd, &ps);
		// TODO: Add any drawing code that uses hdc here...

		EndPaint(hWnd, &ps);
	}
	break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hWnd, msg, wParam, lParam);
	}
	return 0;
}