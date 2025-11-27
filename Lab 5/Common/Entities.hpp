#pragma once
#include <windows.h>
#include <string>

const std::string PIPE_NAME = "\\\\.\\pipe\\EmployeePipe";

struct Employee {
	int num;
	char name[31];
	double hours;
};

enum class RequestType {
	READ,
	MODIFY
};

struct Request {
	RequestType type;
	int employeeNum;
};

struct Response {
	bool found;
	Employee record;
	char message[256];
};

enum class EndAction {
	SAVE,
	FINISH
};