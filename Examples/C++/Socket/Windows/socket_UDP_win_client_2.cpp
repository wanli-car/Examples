#include <stdio.h>
#include <WinSock2.h>
#pragma comment(lib, "ws2_32.lib")  //加载 ws2_32.dll

#define BUF_SIZE 100

int main(){
    //初始化DLL
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    //创建套接字
    SOCKET sock = socket(PF_INET, SOCK_DGRAM, 0);

    //服务器地址信息
    sockaddr_in servAddr;
    memset(&servAddr, 0, sizeof(servAddr));  //每个字节都用0填充
    servAddr.sin_family = PF_INET;
    servAddr.sin_addr.s_addr = inet_addr("192.168.31.159");
    servAddr.sin_port = htons(1234);

    //不断获取用户输入并发送给服务器，然后接受服务器数据
    sockaddr fromAddr;
    int addrLen = sizeof(fromAddr);
    char buffer[BUF_SIZE] = {0};
    while(1){
        memset(buffer, 0, BUF_SIZE);
        printf("Input a string: ");
        gets(buffer);

        sendto(sock, buffer, strlen(buffer), 0, (struct sockaddr*)&servAddr, sizeof(servAddr));
        printf("send ok");
        //memset(buffer, 0, BUF_SIZE);
        int strLen = recvfrom(sock, buffer, BUF_SIZE, 0, &fromAddr, &addrLen);
        if(strLen>0){
        //buffer[strLen] = 0;
        printf("Message form server: %s\n", buffer);
        }
    }

    closesocket(sock);
    WSACleanup();
    return 0;
}