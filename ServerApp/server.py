import json
import time
import datetime

class Board:
    threads = []
    def __init__(self, title):
        self.title = title
    
    def add_thread(self, thread_title, thread_author): #Add required image
        new_thread = Thread(thread_title, thread_author)
        self.threads.append(new_thread)
    
    def update(self):
        threads_in_json = []
        for thread in self.threads:
            thread.update()
            threads_in_json.append(thread.in_json)
        board_data = {"title":self.title, "threads":threads_in_json}
        self.in_json = json.dumps(board_data, sort_keys=False, indent=2)

class Thread:

    def __init__(self, title, author):
        if title == None:
            self.title = "Thread"
        else:
            self.title = title
        if author == None:
            self.author = "Anonymous"
        else:
            self.author = author
        self.timestamp = datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S')
        self.posts = []
        
        #self.image = image
        
        self.update()
    
    def add_post(self, post):
        self.posts.append(post.in_json)
    
    def update(self):
        self.in_json = {"title":self.title, "author":self.author, "timestamp":self.timestamp, "posts":self.posts}
        
class Post:
    
    def __init__(self, title, text, author): #Add optional image
        if title == None:
            self.title = "Comment"
        else:
            self.title = title
        if author == None:
            self.author = "Anonymous"
        else:
            self.author = author
        self.text = text
        self.timestamp = datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S')
        self.in_json = {"title":self.title, "author":self.author, "timestamp":self.timestamp, "text":self.text}
        

manChan = Board("manChan")
manChan.add_thread("Sauce?", None)
post = Post(None, "This thread blows", "Weston")
manChan.threads[0].add_post(post)
manChan.update()
print manChan.in_json

