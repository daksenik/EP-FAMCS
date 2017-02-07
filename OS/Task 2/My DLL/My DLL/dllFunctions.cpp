#include "dllFunctions.h"
#include <Windows.h>

int myDLLFuncs::sum(int a, int b) {
	return a + b;
}
void myDLLFuncs::show(char* s) {
	MessageBox(NULL, s, "Caption",MB_OK);
}