#include <stdio.h>
#include <Windows.h>
#include <stdlib.h>
#include <conio.h>
#include <assert.h>

int main(int argc, char*argv[]) {
	char s[256];

	char hProcb[100];
	GetEnvironmentVariable("PROCHANDLE_PROCB", hProcb, sizeof(hProcb));

	char hBID[100];
	GetEnvironmentVariable("PROCID_PROCB", hBID,sizeof(hBID));

	sprintf(s, "Handle : %s\nID : %s\n", hProcb, hBID);

	//sprintf(s, "ProcessC resumed by ProcessA, hProcessB = %s, B's PID = %s", argv[1], argv[2]);

	MessageBox(NULL, TEXT(s), TEXT("1:ProcessC"), MB_OK);

	DWORD dwBProcessId = atoi(hBID);
	HANDLE hargB = (HANDLE)atoi(hProcb);

	HANDLE hProcessB = OpenProcess(PROCESS_DUP_HANDLE, FALSE, dwBProcessId);
	assert(hProcessB);
	assert(hProcessB != hargB);

	if (TerminateProcess(hargB, 2))MessageBox(NULL, TEXT("Inherited ProcessB terminated"), TEXT("2:ProcessC"), MB_OK);
		else MessageBox(NULL, TEXT("ProcessB failed to be terminated"), TEXT("2:ProcessC"), MB_OK);	

	CloseHandle(hargB);
	CloseHandle(hProcessB);

	printf("\nProcessC\nPress any key to exit!\n");
	getch();
	return 0;
}