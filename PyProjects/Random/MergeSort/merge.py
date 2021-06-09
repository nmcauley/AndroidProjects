import random
from random import randint


def merge_sort(arr):
    if len(arr) > 1:
        mid = len(arr) // 2
        # left array split
        L = arr[:mid]

        # right array
        R = arr[mid:]

        # recursive call on halves
        merge_sort(L)
        merge_sort(R)

        # compare values from left and right into main arr
        i = j = k = 0
        while i < len(L) and j < len(R):
            if L[i] < R[j]:
                arr[k] = L[i]
                i += 1
            else:
                arr[k] = R[j]
                j += 1
            k += 1
        # cleanup any leftover values
        while i < len(L):
            arr[k] = L[i]
            i += 1
            k += 1

        while j < len(R):
            arr[k] = R[j]
            j += 1
            k += 1


if __name__ == "__main__":
    array = []
    print(array)
    merge_sort(array)
    print(array)
