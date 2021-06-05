import socket
import threading

HEADER = 64
# port and server
PORT = 5050
# obtain IP address
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = "utf-8"
DISCONNECT_MSG = "!DISCONNECT"

# bind socket to that address
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)


# input -> connection, address
def handle_client(conn, addr):
    print(f"[NEW CONNECTION] {addr} connected")
    connected = True
    while connected:
        msg_length = conn.recv(HEADER).decode(FORMAT)
        # checks if msg is not empty
        if msg_length:
            msg_length = int(msg_length)
            msg = conn.recv(msg_length).decode(FORMAT)
            if msg == DISCONNECT_MSG:
                connected = False

            print(f"[{addr}] {msg}")
            # send confirmation
            conn.send("Msg received".encode(FORMAT) if msg != DISCONNECT_MSG
                      else "Thanks for chatting!".encode(FORMAT))

    conn.close()


def start():
    server.listen()
    print(f"[LISTENING] server is listening on {SERVER}")
    while True:
        conn, addr = server.accept()
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()
        print(f"[ACTIVE CONNECTIONS] {threading.active_count() - 1}")


print("[STARTING] server is now starting...")
start()
