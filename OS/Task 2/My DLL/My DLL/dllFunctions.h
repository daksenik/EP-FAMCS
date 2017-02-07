#pragma once
#include <string>
using namespace std;

class myDLLFuncs {
public:
	static __declspec(dllexport) int sum(int a, int b);
	static __declspec(dllexport) void show(char* s);
};

