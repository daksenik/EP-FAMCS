#include <Windows.h>
#include <stdio.h>
#include <conio.h>
#include <assert.h>

using namespace std;

int main(int argc, char*argv[])
{
	if (argc < 3) {
		printf("\nProcessC: Error argc<3\n");
		getch();
		return 0;
	}
	char s[256];
	
	sprintf(s, "ProcessC resumed by ProcessA, hProcessB = %s, A's PID = %s", argv[1], argv[2]);

	MessageBox(NULL, TEXT(s), TEXT("1:ProcessC"), MB_OK);

	DWORD dwAProcessId = atoi(argv[2]);
	HANDLE hargB = (HANDLE)atoi(argv[1]);

	HANDLE hProcessA = OpenProcess(PROCESS_DUP_HANDLE, FALSE, dwAProcessId);

	HANDLE hBC;

	DuplicateHandle(hProcessA, hargB, GetCurrentProcess(), &hBC, 0,FALSE, DUPLICATE_SAME_ACCESS);
	

	if (TerminateProcess(hBC, 2))MessageBox(NULL, TEXT("Inherited ProcessB terminated"), TEXT("2:ProcessC"), MB_OK);
	else MessageBox(NULL, TEXT("ProcessB failed to be terminated"), TEXT("2:ProcessC"), MB_OK);

	CloseHandle(hBC);
	CloseHandle(hargB);
	CloseHandle(hProcessA);

	printf("\nProcessC\nPress any key to exit!\n");
	getch();
	return 0;
}
