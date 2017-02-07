#include <Windows.h>
#include <conio.h>
#include <process.h>
#include <iostream>

struct handlesRW {
	HANDLE readSource;
	HANDLE writeSource;
};

handlesRW run1;
handlesRW run2;

unsigned int __stdcall WriteProcess(void*);
unsigned int __stdcall ReadProcess(void*);

HANDLE GetFileHandle(char *pFilePath);

HANDLE hThreads[3];
unsigned threadIDs[3];

int main(int argc, char*argv[])
{
	printf("\nMainThread starting...");

	HANDLE pipe1Read, pipe1Write, pipe2Read, pipe2Write;

	if (!CreatePipe(&pipe1Read, &pipe1Write, NULL, 200))
	{
		perror("\nError creating 1st pipe.");
		return 1;
	}
	
	hThreads[0] = (HANDLE)_beginthreadex(NULL, 0, WriteProcess, &pipe1Write, !CREATE_SUSPENDED, &threadIDs[0]);
	if (!hThreads[0])
	{
		perror("\nError creating write process [0].");
		return 1;
	}
	printf("\nWrite process started. [0]");

	if (!CreatePipe(&pipe2Read, &pipe2Write, NULL, 200))
	{
		perror("\nError creating 2nd pipe.");
		return 1;
	}

	run1.readSource = pipe1Read;
	run1.writeSource = pipe2Write;

	hThreads[1] = (HANDLE)_beginthreadex(NULL, 0, ReadProcess, &run1, !CREATE_SUSPENDED, &threadIDs[1]);
	if (!hThreads[1])
	{
		perror("\nError creating ReadProcess process [1].");
		return 1;
	}
	printf("\nRead process started. [1]");

	run2.readSource = pipe2Read;
	run2.writeSource = GetFileHandle("out.txt");

	hThreads[2] = (HANDLE)_beginthreadex(NULL, 0, ReadProcess, &run2, !CREATE_SUSPENDED, &threadIDs[2]);
	if (!hThreads[2])
	{
		perror("\nError creating ReadProcess process [2].");
		return 1;
	}
	printf("\nRead process started. [2]");

	DWORD dwWait = WaitForMultipleObjects(3, hThreads, TRUE, INFINITE);

	switch (dwWait)
	{
		case WAIT_FAILED:   
			printf("\nWAIT_FAILED");
			break;
		case WAIT_OBJECT_0: break;
		case WAIT_OBJECT_0 + 1: 
			printf("\nWAIT_OBJECT_i:=%d", dwWait);
			break;
		case WAIT_OBJECT_0 + 2:
			printf("\nWAIT_OBJECT_i:=%d", dwWait);
			break;
		case WAIT_TIMEOUT:
			printf("WAIT_TIMEOUT");
			break;
		default:
			printf("\nCompleted\n");
	}

	printf("\nMainThread finishing...");
	getch();
}


HANDLE GetFileHandle(char *pFilePath)
{
	printf("GetFileHandle(%s)", pFilePath);
	HANDLE hStdOutFile = GetStdHandle(STD_OUTPUT_HANDLE);
	if (INVALID_HANDLE_VALUE == hStdOutFile)return INVALID_HANDLE_VALUE;
	if (!pFilePath)return hStdOutFile;

	HANDLE	 hFile = CreateFile(pFilePath,GENERIC_READ | GENERIC_WRITE,0,NULL,CREATE_ALWAYS,FILE_ATTRIBUTE_ARCHIVE,NULL);
	if(INVALID_HANDLE_VALUE == hFile)return hStdOutFile;

	if (!SetStdHandle(STD_OUTPUT_HANDLE, hFile))
	{
		CloseHandle(hFile);
		return hStdOutFile;
	}
	return hFile;
}

