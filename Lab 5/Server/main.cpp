#include "Server.hpp"

int main() {
	initializeSync();

	std::string filename;
	int clientCount = 0;

	HANDLE hFile = createDatabase(filename, clientCount);
	launchClients(clientCount);
	runServer(hFile, clientCount);

	CloseHandle(hFile);
	std::cout << "\nAll clients finished. Final result:";
	printFileContent(filename);

	std::cout << "Press any key to exit...";
	char c;
	std::cin >> c;

	cleanupSync();
	return 0;
}