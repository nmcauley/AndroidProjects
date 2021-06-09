# simple adjacency graph; has functionality for single and bidirectional edges
class AdjGraph(object):

    # constructor: takes the first vertex for the graph
    def __init__(self, init):
        # null constructor creates an empty adjacency graph to add vertices to
        self.adjMatrix = []
        self.vertices = []
        self.size = 0
        if init is not None:
            self.add_vertex(init)

    # adds vertex to adjMatrix
    def add_vertex(self, v):
        if v in self.adjMatrix:
            print(f"Vertex {v} already exists")
            return
        self.size += 1
        self.vertices.append(v)

        # add a new place for the added vertex
        if self.size > 1:
            for vertex in self.adjMatrix:
                vertex.append(0)

        temp = []
        for i in range(self.size):
            temp.append(0)
        # initialized new vertex array to be added to the matrix
        self.adjMatrix.append(temp)

    # adding edges; isBiDir checks whether to have edges pointing both ways or just from v1 -> v2
    def add_edge(self, v1, v2, is_bi_dir):
        if v1 == v2:
            print(f"{v1} is the same as {v2}")

        # obtain the proper index from the vertices[]
        # we'll use this when assigning values
        i = self.vertices.index(v1)
        j = self.vertices.index(v2)

        self.adjMatrix[i][j] = 1
        # check for bidirectional
        if is_bi_dir:
            self.adjMatrix[j][i] = 1

    # removes both directions of the graph if one exists v1 <-> v2
    def remove_both_edges(self, v1, v2):
        if v1 == v2:
            print(f"{v1} is the same as {v2}")
        self.adjMatrix[v1][v2] = 0
        self.adjMatrix[v2][v1] = 0

    # only removes the edge from v1 -> v2
    def remove_edge(self, v1, v2):
        if v1 == v2:
            print(f"{v1} is the same as {v2}")
        self.adjMatrix[v1][v2] = 0

    def __len__(self):
        return self.size

    # Print the vertices and their connections in format : [v, weight]
    def print_vertex_graph(self):
        for i in range(self.size):
            each = str(self.vertices[i]) + ": "
            # obtain all neighbors
            for j in self.adjMatrix[i]:
                # checks if anything other than 0 (in event of weighted edge)
                if j > 0:
                    # obtain vertex and it's edge weight
                    each += "[" + self.vertices[j] + ", " + str(j) + "] "
            print(each)

    # Print the matrix
    def print_matrix(self):
        header = "   "
        body = ""
        for i in range(self.size):
            header += self.vertices[i] + "  " if i != self.size - 1 else self.vertices[i] + " "
            body += self.vertices[i] + " " + str(self.adjMatrix[i]) + "\n"
        print(header)
        print(body)
        # print(*self.adjMatrix, sep="\n")

        # todo add dijkstra and prim algorithm
    def dijkstra(self):
        pass

    def prim(self):
        pass


def main():
    g = AdjGraph("A")
    g.add_vertex("V")
    g.add_edge("A", "V", False)
    g.add_vertex("B")
    g.print_matrix()
    g.print_vertex_graph()


if __name__ == '__main__':
    main()
