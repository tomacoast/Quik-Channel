import json
import time
import thread
import datetime
import socket
import sys
from PIL import Image

class Board:
	threads = []
	def __init__(self, title):
		self.title = title
		#self.json_file = open("board.json", "w"
		self.post_id_counter = 0
		#self.json_file_data = simplejson.loads(self.json_file)
	
	def add_thread(self, thread_title, thread_text, thread_author): #Add required image
		self.post_id_counter = self.post_id_counter + 1
		post_id = self.post_id_counter
		new_thread = Thread(thread_title, thread_text, thread_author, post_id)
		self.threads.append(new_thread)
		return post_id
	
	def add_post(self, thread, title, text, author):
		self.post_id_counter = self.post_id_counter + 1
		post_id = self.post_id_counter
		new_post = Post(title, text, author, post_id) 
		thread.add_post(new_post)
		return new_post
	
	def update(self):
		self.json_file = open("board.json", "w")
		threads_in_json = []
		for thread in self.threads:
			thread.update()
			threads_in_json.append(thread.in_json)
		board_data = {"title":self.title, "threads":threads_in_json, "id_counter":self.post_id_counter}
		self.in_json = json.dumps(board_data, sort_keys=False, indent=2)
		for line in self.in_json:
			self.json_file.write(line)
		self.json_file.close()
		
	def set_id(self, id_num):
		self.post_id_counter = id_num

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
	
	#Adds received json to board
	def add_to_loaded(self, data, board):
		self.board = board
		#self.json_data = open(json_file_path)
		#data = json.load(self.json_data)
		if data["threadid"] == -1:
			return board.add_thread(data["title"], data["text"], data["author"])
		else:
			for thread in board.threads:
				if thread.thread_id == data["threadid"]:
					return board.add_post(thread, data["title"], data["text"], data["author"]).post_id
	
	#Loads board if server ever stopped
	def load_backup(self, backup_path):
		json_file = open(backup_path)
		data = json.load(json_file)
		board = Board(data["title"])
		for thread in data["threads"]:
			new_thread = Thread(thread["entries"][0]["title"], thread["entries"][0]["text"], thread["entries"][0]["author"], thread["entries"][0]["id"])
			board.threads.append(new_thread)
			for entry in thread["entries"]:
				if entry["id"] == thread["entries"][0]["id"]:
					pass
				else:
					new_post = Post(entry["title"], entry["text"], entry["author"], entry["id"])
					new_thread.add_post(new_post)
		board.set_id(data["id_counter"])
		return board
		

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
		for i in range(1, len(data)):
			if ord(data[i]) == 217 and ord(data[i-1]) == 255:
				return data[0:len(data)-2] #removing the EOI bits in cas it's not an image
	return data

def writeToFile(data, filename):
	f = open(filename, "wb")
	f.write(data)
	f.close()

	#now make a resized version
	f = open(filename + '.thum', 'wb')
	im = Image.open(filename)
	im.thumbnail((128, 128))
	im.save(f, "JPEG")


def serveClient(client, address, board):
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
		if imgdata != None:
			imgdata = imgdata + (chr(255) + chr(217)) #adding the EOI bits back to the image
		loadedData = json.loads(jsondata)
		print jsondata
		print "found json data :" + str(loadedData)
		if loadedData['request'] == False: #if it's not a request
			#write the image to a file with the name of the post id if the image is there
			loader =JSON_Board_Loader()
			post = loader.add_to_loaded(loadedData, board)
			if imgdata:
				print "saving an image to " + str(post) + ".jpg"
				writeToFile(imgdata, str(post) + ".jpg")
			#writes the json to a post
			board.update()
		else: #send requested data to the client
			board.update()
			if loadedData['request'] == 'board':
				client.send(board.in_json) #send json data
			if loadedData['request'] == 'image':
				try:
					img = open(loadedData['image'], 'r') #send the image
					client.send(img.read())
					print 'sent data about image ' + loadedData['image']
					img.close()
				except IOError:
					print 'no image \"' + loadedData['image'] + '\"'

	client.close()
	print "client closed!"

loader = JSON_Board_Loader()
board_file = open("board.json", "r+")
board_file.seek(0)
if board_file.read(1) == '{':
	board = loader.load_backup("board.json")
else:	
	board = Board("board")
	board.add_thread("First Thread", "Board created successfully", "Admin")
board_file.close()
board.update()
host = ''
port = 8123
if (len(sys.argv) > 1):
	port = int(sys.argv[1])
print port
backlog = 5
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog)
while 1:
	client, address = s.accept() 
	print "client recieved at " + address[0]
	thread.start_new_thread(serveClient, (client, address, board))
