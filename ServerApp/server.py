import json
import time
import thread
import datetime
import socket

class Board:
	threads = []
	def __init__(self, title):
		self.title = title
		self.json_file = open("board.json", "w")
		self.post_id_counter = 0
		#self.json_file_data = simplejson.loads(self.json_file)
	
	def add_thread(self, thread_title, thread_text, thread_author): #Add required image
		self.post_id_counter = self.post_id_counter + 1
		post_id = self.post_id_counter
		new_thread = Thread(thread_title, thread_text, thread_author, post_id)
		self.threads.append(new_thread)
	
	def add_post(self, thread, title, text, author):
		self.post_id_counter = self.post_id_counter + 1
		post_id = self.post_id_counter
		new_post = Post(title, text, author, post_id) 
		thread.add_post(new_post)
	
	def update(self):
		threads_in_json = []
		for thread in self.threads:
			thread.update()
			threads_in_json.append(thread.in_json)
		board_data = {"title":self.title, "threads":threads_in_json}
		self.in_json = json.dumps(board_data, sort_keys=False, indent=2)
		for line in self.in_json:
			self.json_file.write(line)

class Thread:

	def __init__(self, title, text, author, thread_id):
		if title == None:
			self.title = "Thread"
		else:
			self.title = title
		if text == None:
			self.text = "No Text"
		else:
			self.text = text
		if author == None:
			self.author = "Anonymous"
		else:
			self.author = author
		self.posts = []
		self.thread_id = thread_id
		new_post = Post(self.title, self.text, self.author, thread_id)
		self.add_post(new_post)
		
		#self.image = image
		
		self.update()
	
	def add_post(self, post):
		self.posts.append(post.in_json)
	
	def update(self):
		self.in_json = {"entries":self.posts}
		
class Post:
	
	def __init__(self, title, text, author, post_id): #Add optional image
		if title == None:
			self.title = "Comment"
		else:
			self.title = title
		if author == None:
			self.author = "Anonymous"
		else:
			self.author = author
		self.post_id = post_id
		self.text = text
		self.timestamp = datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S')
		self.in_json = {"title":self.title, "author":self.author, "id":self.post_id, "timestamp":self.timestamp, "text":self.text}

class JSON_Board_Loader:
	
	def add_to_loaded(self, json_file_path, board):
		self.board = board
		self.json_data = open(json_file_path)
		data = json.load(self.json_data)
		if data["threadid"] == -1:
			board.add_thread(data["title"], data["text"], data["author"])
		else:
			for thread in board.threads:
				if thread.thread_id == data["threadid"]:
					board.add_post(thread, data["title"], data["text"], data["author"])

def recvall(sock):
	data = ""
	part = None
	while part != "":
		part = sock.recv(4096)
		data+=part
	return data

def recvUntilNewline(sock):
	data = ""
	part = None
	while part != "":
		part = sock.recv(4096)
		data += part
		if '\n' in part:
			break
	return data

def recvUntilEOI(sock):
	data = ""
	part = None
	while part != "":
		part = sock.recv(4096)
		data+=part
		for i in range(1, len(part)):
			if ord(part[i]) == 217 and ord(part[i-1]) == 255:
				return data
	return data

def writeToFile(data, filename):
	f = open(filename, "wb")
	f.write(data)

def serveClient(client, address):
	data = recvUntilEOI(client) #getting the initial data
	if data:
		jsondata = data
		imgdata = None
		for i in range(len(data) - 1):
			#looks for the jpg header
			if ord(data[i]) == 255 and ord(data[i+1]) == 216:
				imgdata = data[i:len(data)] #when it finds it, it assigns it to imgdata
				jsondata = data[0:i] #and the rest to jsondata
				break
		loadedData = json.loads(jsondata)
		#print loadedData
		#print imgdata
		print loadedData['request']
		if loadedData['request'] == False: #if it's not a request
			if (loadedData['threadid'] >= 0): #adding a post to a thread
				print "writing!"
				#write the image to a file with the name of the post id if the image is there
				if imgdata:
					writeToFile(imgdata, str(loadedData['threadid']) + ".jpg")
			else: #adding a thread to a board
				pass
		else: #send requested data to the client
			if loadedData['request'] == 'board':
				client.send('data!\n') #send json data
			if loadedData['request'] == 'image':
				img = open(loadedData['image']) #send the image
				client.send(img.read())
				img.close()

	client.close()
	print "client closed!"

# loader = JSON_Board_Loader()
# manChan = Board("manChan")
# manChan.add_thread("Sauce?", None, None)
# manChan.add_post(manChan.threads[0], "Why I Hate QuikChan", "Does nobody ever post anything goohd?", None)
# manChan.add_post(manChan.threads[0], None, "This thread blows", "Weston")
# loader.add_to_loaded("new_thread_temp.json", manChan)
# loader.add_to_loaded("new_post_temp.json", manChan)
# manChan.update()
# print manChan.in_json

host = ''
port = 42080
backlog = 5
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog)
while 1:
	client, address = s.accept() 
	print "client recieved!"
	thread.start_new_thread(serveClient, (client, address))