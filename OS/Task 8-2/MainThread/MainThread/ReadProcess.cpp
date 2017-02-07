#include <iostream>
#include <Windows.h>
#include <conio.h>
#include <stdio.h>

struct handlesRW {
	HANDLE readSource;
	HANDLE writeSource;
};

handlesRW pars;

unsigned int __stdcall ReadProcess(void*argList)
{
	printf("\nReadProcess starting...");
	pars = *(handlesRW*)argList;

	char buffer[100];
	BOOL bRes;
	DWORD bytesRead;

	for (int i = 0; i < 100;i++)
	{
		bytesRead = 0;
		bRes = ReadFile(pars.readSource, buffer, 1, &bytesRead, NULL);
		if (bRes&&bytesRead == 0)break;
		bytesRead = 0;
		bRes = WriteFile(pars.writeSource, buffer, 1, &bytesRead, NULL);
	}

	sprintf(buffer, "ReadProcess finishing...");
	WriteFile(pars.writeSource, buffer, strlen(buffer), &bytesRead, NULL);
	CloseHandle(pars.readSource);
	CloseHandle(pars.writeSource);

	printf("\nReadProcess finishing...");
	return 0;
}