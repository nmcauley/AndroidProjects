import socket

HEADER = 64
# port and server
PORT = 5050
FORMAT = "utf-8"
DISCONNECT_MSG = "!DISCONNECT"
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)


def send(msg):
    # msg = input("Type your msg!")
    # while msg != "esc":
    message = msg.encode(FORMAT)
    msg_length = len(message)
    send_length = str(msg_length).encode(FORMAT)

    # we need to pad to our designated 64 length
    send_length += b' ' * (HEADER - len(send_length))
    client.send(send_length)
    client.send(message)
    print(client.recv(2048).decode(FORMAT))


send("u_msg")
send(DISCONNECT_MSG)
