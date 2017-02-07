#include <Windows.h>
#include <process.h>
#include <time.h>
#include <ctime>
#include "stdafx.h"

#define STATE_THINKING 0
#define STATE_TAKINGFORKS 1
#define STATE_EATING 2
#define STATE_PUTTINGFORKS 3

LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);

const int N = 11;

bool wndReady = false;

HWND window;

HANDLE*semaphores = new HANDLE[N + 1];

HANDLE*pThreads = new HANDLE[N];

int*state = new int[N];
int*number = new int[N];

void think(int);
void take_forks(int);
void eat(int);
void put_forks(int);
void setState(int, int);

unsigned int __stdcall philosopher(void* k)
{
	int numb = *((int*)k);
	while (!wndReady)Sleep(1000);
	while (true)
	{
		think(numb);
		Sleep(1000 + rand() % 100);
		take_forks(numb);
		Sleep(1000 + rand() % 100);
		eat(numb);
		Sleep(1000 + rand() % 100);
		put_forks(numb);
		Sleep(1000 + rand() % 100);
	}
}

void put_forks(int numb)
{
	if (state[numb] == STATE_EATING)
	{
		ReleaseSemaphore(semaphores[numb + 1], 1, NULL);
		ReleaseSemaphore(semaphores[numb], 1, NULL);
		setState(numb, STATE_PUTTINGFORKS);
	}
}

void eat(int numb)
{
	if (state[numb] == STATE_TAKINGFORKS)
	{
		setState(numb, STATE_EATING);
	}
}

void take_forks(int numb)
{
	if (state[numb] != STATE_THINKING)return;
	DWORD fOk = WaitForMultipleObjects(2, semaphores + numb, TRUE, INFINITE);
	if (fOk == WAIT_OBJECT_0)
	{
		setState(numb, STATE_TAKINGFORKS);
	}
}

void think(int numb)
{
	setState(numb, STATE_THINKING);
}

void setState(int numb, int stt)
{
	state[numb] = stt;
	RECT*r = new RECT();
	r->left = 10 + 20 * numb;
	r->right = r->left + 10;
	r->top = 10;
	r->bottom = 20;
	if (wndReady)InvalidateRect(window, r, true);
}

int APIENTRY wWinMain(HINSTANCE hInstance, HINSTANCE, PTSTR pszCmdLine, int nCmdShow) {
	WNDCLASSEX wc;

	srand(time(NULL));

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


	HWND hWnd = CreateWindow(szWindowClass, L"N pholosophers problem", WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT, 500, 500, NULL, NULL, hInstance, NULL);

	if (!hWnd)
	{
		MessageBox(NULL, L"CreateWindow failed!", L"Error", NULL);
		return 1;
	}

	window = hWnd;

	ShowWindow(hWnd, nCmdShow);
	UpdateWindow(hWnd);

	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	return (int)msg.wParam;
}

LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	HBRUSH hBrush;
	HDC hdc;
	int x, y;
	switch (msg)
	{
	case WM_CREATE:
		for (int i = 0; i < N; i++)
		{
			state[i] = 0;
			semaphores[i] = CreateSemaphore(NULL, 1, 1, NULL);
			number[i] = i;
			pThreads[i] = (HANDLE)_beginthreadex(NULL, 0, philosopher, &number[i], !CREATE_SUSPENDED, NULL);
		}
		semaphores[N] = semaphores[0];
		break;
	case WM_PAINT:
		wndReady = true;
		PAINTSTRUCT ps;
		hdc = BeginPaint(hwnd, &ps);
		x = 10;
		y = 10;
		for (int i = 0; i < N; i++)
		{
			int red = 0, green = 0, blue = 0;
			switch (state[i]) {
			case STATE_THINKING:
				blue = 255;
				break;
			case STATE_TAKINGFORKS:
				red = green = 255;
				break;
			case STATE_EATING:
				green = 255;
				break;
			case STATE_PUTTINGFORKS:
				red = green = 102;
				break;
			}
			hBrush = CreateSolidBrush(RGB(red, green, blue));
			SelectObject(hdc, hBrush);
			Ellipse(hdc, x, y, x + 10, y + 10);
			x += 20;
			DeleteObject(hBrush);
		}

		EndPaint(hwnd, &ps);
		break;
	case WM_DESTROY:
		for (int i = 0; i < N; i++) {
			CloseHandle(semaphores[i]);
			TerminateThread(pThreads[i], 2);
			CloseHandle(pThreads[i]);
		}
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hwnd, msg, wParam, lParam);
	}
	return 0;
}