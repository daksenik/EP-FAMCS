#include <Windows.h>
#include <conio.h>
#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <time.h>

unsigned int __stdcall WriteProcess(void*writeSource)
{
	printf("\nInside the WriteProcess");
	srand(time(0));

	char randInt[100];
	DWORD dwWritten = 0;
	BOOL bRes;

	for (int i = 0; i < 100; i++) {
		sprintf(randInt, "%d", rand());
		dwWritten = 0;
		bRes = WriteFile(*(HANDLE*)writeSource, randInt, strlen(randInt) + 1, &dwWritten, NULL);
		if (!bRes)
		{
			perror("\nWriteProc [0] : writing error (failed).");
			break;
		}
		if (dwWritten < strlen(randInt) + 1)
		{
			perror("\nWriteProc [0] : writing error.");
			break;
		}
	}
	CloseHandle(*(HANDLE*)writeSource);
	printf("\nWrite process finishing... [0]");
	return 0;
}