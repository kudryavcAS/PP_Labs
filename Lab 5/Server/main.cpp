#include "Server.hpp"

int main() {
#ifdef _WIN32
	SetConsoleTitle("Server");
#endif

	initializeSync();

	std::string filename;
	int clientCount = 0;

	HANDLE hTempHandle = createDatabase(filename, clientCount);
	CloseHandle(hTempHandle);

	launchClients(clientCount);

	runServer(filename, clientCount);

	std::cout << "\nAll clients finished. Final result:";
	printFileContent(filename);

	std::cout << "Press any key to exit.";
	(void)_getch();

	cleanupSync();
	return 0;
}