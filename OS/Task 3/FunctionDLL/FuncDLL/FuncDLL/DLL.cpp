#include <Windows.h>
#include <iostream>

#define STRING1 0
#define STRING2 1
#define STRING3 2

extern "C"
{
	__declspec(dllexport) TCHAR* getString(int a)
	{
		HINSTANCE hI = GetModuleHandle(NULL);
		TCHAR string[160];
		switch (a) {
		case 0:
			LoadString(hI, STRING1, string, sizeof(string) / sizeof(TCHAR));
			break;
		case 1:
			LoadString(hI, STRING2, string, sizeof(string) / sizeof(TCHAR));
			break;
		case 2:
			LoadString(hI, STRING3, string, sizeof(string) / sizeof(TCHAR));
			break;
		default:
			break;
		}
		MessageBox(NULL, string, L"DLL", 0);
		return string;
	}
}
