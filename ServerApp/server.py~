import json

class Board:
    threads = []
    def __init__(self, title):
        self.title = title
    
    def add_thread(self, thread_title):
        new_thread = Thread(thread_title)
        self.threads.append(new_thread)
    
    def update(self):
        threads_in_json = []
        for thread in self.threads:
            thread.update()
            threads_in_json.append(thread.json_data)
        board_data = [{"title":self.title, "threads":threads_in_json}]
        self.in_json = json.dumps(board_data, sort_keys=False, indent=2)

class Thread:
    posts = []
    data = []
    title = ''
    def __init__(self, title):
        self.title = title
    
    def add_post(self, text):
        self.posts.append(text)
    
    def update(self):
        self.json_data = [{"title":self.title, "posts":self.posts}]

manChan = Board("manChan")
manChan.add_thread("Sauce?")
manChan.threads[0].add_post("bump")
manChan.threads[0].add_post("Try the deep web")
manChan.add_thread("bawwww thread")
while(True):
    text = raw_input()
    manChan.threads[1].add_post(text)
    manChan.update()
    print manChan.in_json

