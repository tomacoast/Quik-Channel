import socket 

def recvall(sock):
    data = ""
    part = None
    while part != "":
        part = sock.recv(4096)
        data+=part
    return data

def writeToFile(data):
	f = open("image.jpg", "wb")
	f.write(data)

host = '' 
port = 42072 
backlog = 5 
size = 2147483647
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog) 
while 1: 
    client, address = s.accept() 
    print "client recieved!"
    data = recvall(client)
    if data: 
    	print "data recieved!"
    	writeToFile(data)
    client.close()
    print "client closed!"

