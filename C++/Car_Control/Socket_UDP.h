#ifndef _SOCKET_UDP_H
#define _SOCKET_UDP_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>

void Socket_Init();

void Socket_Task();

void Socket_Close();


#endif