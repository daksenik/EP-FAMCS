#include <stdio.h>
#include <conio.h>
#include <iostream>
#include <Windows.h>

int main(int argc, char*argv[])
{
	setlocale(LC_ALL, "RUS");
	printf("BReaderWriter starting...\n");

	int c, n = 0;
	while ((c = getc(stdin)) != EOF)if (n < 120)
	{
		putc(c, stdout);
		n++;
	}
	printf("\nBReaderWriter finishing...\n");
}