#include <Windows.h>
#include <stdio.h>
#include <conio.h>

void ErrorExit(LPTSTR);
void GetProcPaths(char *cmd[], char *WritePath, char *ReadPath, char *FilePath);

BOOL CreateChildProcess(LPTSTR szCommandLine, BOOL bInherit, PROCESS_INFORMATION *ppiProcInfo);
HANDLE RedirectStdoutToFile(char *FilePath);

HANDLE hSaveStdout, hSaveStdIn,hPipeRd, hPipeWr,hPipeRdDup, hPipeWrDup,hFile;
HANDLE hPipeRd2, hPipeWr2, hPipeRd2Dup, hPipeWr2Dup;

PROCESS_INFORMATION piProcInfoWrite, piProcInfoRead, piProcInfo2;

int main(int argc, char*argv[])
{
	char WritePath[300], ReadPath[300], FilePath[300];
	WritePath[0] = 0; ReadPath[0] = 0; FilePath[0] = 0;
	if (argc != 4)ErrorExit("Not enough arguments!");

	GetProcPaths(argv, WritePath, ReadPath, FilePath);
	printf("WritePath=%s\nReadPath=%s\nFilePath=%s\n", WritePath, ReadPath, FilePath);

	if (WritePath[0] == 0 || ReadPath[0] == 0)ErrorExit("One of two process paths is an empty string!");

	printf("MainProc starting...!\n");

	SECURITY_ATTRIBUTES saAttr,saAttr2;

	// Set the bInheritHandle flag so pipe handles are inherited. 
	saAttr.nLength = sizeof(SECURITY_ATTRIBUTES);
	saAttr.bInheritHandle = TRUE;
	saAttr.lpSecurityDescriptor = NULL;

	saAttr2.nLength = sizeof(SECURITY_ATTRIBUTES);
	saAttr2.bInheritHandle = TRUE;
	saAttr2.lpSecurityDescriptor = NULL;

	if (!CreatePipe(&hPipeRd, &hPipeWr, &saAttr, 200))ErrorExit("Pipe creation failed\n");
	hSaveStdout = GetStdHandle(STD_OUTPUT_HANDLE);
	if (!SetStdHandle(STD_OUTPUT_HANDLE, hPipeWr))ErrorExit("Redirecting STDOUT to hPipeWr failed");
	BOOL  fSuccess = DuplicateHandle(GetCurrentProcess(), hPipeRd,GetCurrentProcess(), &hPipeRdDup,0,FALSE,DUPLICATE_SAME_ACCESS);
	if (!fSuccess) ErrorExit("DuplicateHandle failed");
	CloseHandle(hPipeRd);

	if (!CreateChildProcess(WritePath, TRUE, &piProcInfoWrite))ErrorExit("Create process WriteEnd failed");

	fSuccess = DuplicateHandle(GetCurrentProcess(), hPipeRdDup, GetCurrentProcess(), &hPipeRd, 0, TRUE, DUPLICATE_SAME_ACCESS);
	if (!fSuccess)ErrorExit("DuplicateHandle failed");
	CloseHandle(hPipeRdDup);
	fSuccess = DuplicateHandle(GetCurrentProcess(), hPipeWr, GetCurrentProcess(), &hPipeWrDup, 0, FALSE, DUPLICATE_SAME_ACCESS);
	if (!fSuccess)ErrorExit("DuplicateHandle failed");
	CloseHandle(hPipeWrDup);
	CloseHandle(hPipeWr);

	if (!CreatePipe(&hPipeRd2, &hPipeWr2, &saAttr2, 200))ErrorExit("Second pipe creation failed\n");
	if(!SetStdHandle(STD_OUTPUT_HANDLE, hPipeWr2))ErrorExit("Re-redirecting STDOUT (Pipe 2) failed\n");
	hSaveStdIn = GetStdHandle(STD_INPUT_HANDLE);
	if (!SetStdHandle(STD_INPUT_HANDLE, hPipeRd))ErrorExit("Redirecting STDIn to hPipeRd failed");
	if (!CreateChildProcess(ReadPath, TRUE, &piProcInfoRead))ErrorExit("Create process ReadWriter failed");

/*	fSuccess = DuplicateHandle(GetCurrentProcess(), hPipeRd2Dup, GetCurrentProcess(), &hPipeRd2, 0, TRUE, DUPLICATE_SAME_ACCESS);
	if (!fSuccess)ErrorExit("DuplicateHandle failed");
	CloseHandle(hPipeRd2Dup);
	fSuccess = DuplicateHandle(GetCurrentProcess(), hPipeWr2, GetCurrentProcess(), &hPipeWr2Dup, 0, FALSE, DUPLICATE_SAME_ACCESS);
	if (!fSuccess)ErrorExit("DuplicateHandle failed");
	CloseHandle(hPipeWr2Dup);
	CloseHandle(hPipeWr2);*/

	CloseHandle(hPipeRd);
	CloseHandle(hPipeWr2);

	if (!SetStdHandle(STD_INPUT_HANDLE, hPipeRd2))ErrorExit("Re-redirecting STDIN failed\n");
	if (FilePath[0] != 0)hFile = RedirectStdoutToFile(FilePath);
	if (!CreateChildProcess(ReadPath, TRUE, &piProcInfo2))ErrorExit("Create process ReadWriter failed\n");
	CloseHandle(hPipeRd2);

	if (!SetStdHandle(STD_INPUT_HANDLE, hSaveStdIn))ErrorExit("Re-redirecting STDIN failed\n");
	if (FilePath[0] != 0 && hFile != INVALID_HANDLE_VALUE)
	{
		CloseHandle(hFile);
		if (!SetStdHandle(STD_OUTPUT_HANDLE, hSaveStdout))ErrorExit("Re-redirecting STDOUT failed\n");
	}


	//(13)We can now wait for ending both child processes!
	HANDLE hTwo[3] = { piProcInfoRead.hProcess,piProcInfoWrite.hProcess,piProcInfo2.hProcess };

	DWORD dwWait = WaitForMultipleObjects(3, (CONST HANDLE*)hTwo, TRUE, INFINITE);

	switch (dwWait)
	{
	case WAIT_FAILED:
		printf("\nWAIT_FAILED");
		break;
	case WAIT_OBJECT_0:
		break;
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
		printf("\nBoth completed\n");
	}

	printf("\nMainProc: Finishing...\nPress any key\n");
	getch();
	return 0;
}


void ErrorExit(LPTSTR lpszMessage)
{
	fprintf(stderr, "%s\n", lpszMessage);
	ExitProcess(1);
}

void GetProcPaths(char *cmd[], char *WritePath, char *ReadPath, char *FilePath)
{
	sscanf(cmd[1], "%s", WritePath);
	sscanf(cmd[2], "%s", ReadPath);
	sscanf(cmd[3], "%s", FilePath);
}

BOOL CreateChildProcess(LPTSTR szCommandLine, BOOL bInherit, PROCESS_INFORMATION *ppiProcInfo)
{
	STARTUPINFO siStartInfo;
	ZeroMemory(ppiProcInfo, sizeof(PROCESS_INFORMATION));
	ZeroMemory(&siStartInfo, sizeof(STARTUPINFO));
	siStartInfo.cb = sizeof(STARTUPINFO);

	return CreateProcess(NULL, szCommandLine, NULL, NULL, bInherit, 0, NULL, NULL, &siStartInfo, ppiProcInfo);
}


HANDLE RedirectStdoutToFile(char *FilePath)
{
	HANDLE hFile;
	SECURITY_ATTRIBUTES saAttr;

	saAttr.nLength = sizeof(SECURITY_ATTRIBUTES);
	saAttr.bInheritHandle = TRUE;
	saAttr.lpSecurityDescriptor = NULL;
	hFile = CreateFile(FilePath,GENERIC_READ | GENERIC_WRITE,0,&saAttr,CREATE_ALWAYS,FILE_ATTRIBUTE_ARCHIVE,NULL);

	if(INVALID_HANDLE_VALUE == hFile)return INVALID_HANDLE_VALUE;

	if(!SetStdHandle(STD_OUTPUT_HANDLE, hFile))
	{
		CloseHandle(hFile);      
		return INVALID_HANDLE_VALUE;
	}
	return hFile;
}


