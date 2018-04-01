#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

struct AdjListNode{//Node in Adjacency List
    int dest;
    int weight_dist;
    int weight_veh;
    struct AdjListNode* next;
};

struct AdjList{//Ajacency list
    struct AdjListNode *head;//head of the list
};

struct Graph{//array of adjacency list and size of vertices will be V
    int V;
    struct AdjList* array;  
};

struct AdjListNode* newAdjListNode(int dest,int weight_dist,int weight_veh){
    struct AdjListNode* newNode=(struct AdjListNode*) malloc(sizeof(struct AdjListNode));
    newNode->dest=dest;
    newNode->weight_dist=weight_dist;
    newNode->weight_veh=weight_veh;
    newNode->next=NULL;

    return newNode;
}

struct Graph* createGraph(int V){
    struct Graph* graph=(struct Graph*) malloc(sizeof(struct Graph));
    graph->V=V;

    graph->array=(struct AdjList*) malloc(V * sizeof(struct AdjList));

    for(int i=0;i<V;++i)
        graph->array[i].head=NULL;

    return graph;
}

void addEdge(struct Graph* graph,int src,int dest,int weight_dist,int weight_veh){
    struct AdjListNode *newNode= newAdjListNode(dest,weight_dist,weight_veh);
    newNode->next=graph->array[src].head;
    graph->array[src].head=newNode;

    //since undirected (can be changed for one way road)
    newNode= newAdjListNode(src,weight_dist,weight_veh);
    newNode->next=graph->array[dest].head;
    graph->array[dest].head=newNode;
}

struct MinHeapNode{// Min Heap node
    int v;
    int count_dist;
    int count_veh;
};

struct MinHeap{// Min Heap
    int size;//no of heap nodes present currently
    int capacity;//capacity of min-heap
    int *pos; //needed for decreaseKey()
    struct MinHeapNode **array;
};

struct MinHeapNode* newMinHeapNode(int v,int count_dist,int count_veh){
    struct MinHeapNode* minHeapNode=(struct MinHeapNode*)malloc(sizeof(struct MinHeapNode));
    minHeapNode->v=v;
    minHeapNode->count_dist=count_dist;
    minHeapNode->count_veh=count_veh;

    return minHeapNode;
}

struct MinHeap* createMinHeap(int capacity){
    struct MinHeap* minHeap=(struct MinHeap*)malloc(sizeof(struct MinHeap));
    minHeap->pos=(int*)malloc(capacity * sizeof(int));
    minHeap->size=0;
    minHeap->capacity=capacity;
    minHeap->array=(struct MinHeapNode**)malloc(capacity * sizeof(MinHeapNode*));

    return minHeap;
}

void swapMinHeapNode(struct MinHeapNode** a,struct MinHeapNode** b){
    struct MinHeapNode* t= *a;
    *a=*b;
    *b= t;
}

void minHeapify(struct MinHeap* minHeap, int idx)
{
    int smallest, left, right;
    int equal=INT_MAX;
    smallest = idx;
    left = 2 * idx + 1;
    right = 2 * idx + 2;
 
    if (left < minHeap->size &&
        minHeap->array[left]->count_veh < minHeap->array[smallest]->count_veh )
      smallest = left;
 
    if (right < minHeap->size &&
        minHeap->array[right]->count_veh < minHeap->array[smallest]->count_veh )
      smallest = right;

    if (left < minHeap->size && right < minHeap->size){
        if(minHeap->array[left]->count_veh==minHeap->array[smallest]->count_veh){
            if(minHeap->array[left]->count_dist < minHeap->array[smallest]->count_dist)
                smallest=left;
        }
             
        if(minHeap->array[right]->count_veh==minHeap->array[smallest]->count_veh){
            if(minHeap->array[right]->count_dist < minHeap->array[smallest]->count_dist)
                smallest=right;
        }
    }

 
    if (smallest != idx)
    {
        // The nodes to be swapped in min heap
        MinHeapNode *smallestNode = minHeap->array[smallest];
        MinHeapNode *idxNode = minHeap->array[idx];
 
        // Swap positions
        minHeap->pos[smallestNode->v] = idx;
        minHeap->pos[idxNode->v] = smallest;
 
        // Swap nodes
        swapMinHeapNode(&minHeap->array[smallest], &minHeap->array[idx]);
 
        minHeapify(minHeap, smallest);
    }
}


int isEmpty(struct MinHeap* minHeap)
{
    return minHeap->size == 0;
}

struct MinHeapNode* extractMin(struct MinHeap* minHeap)
{
    if (isEmpty(minHeap))
        return NULL;
 
    // Store the root node
    struct MinHeapNode* root = minHeap->array[0];
 
    // Replace root node with last node
    struct MinHeapNode* lastNode = minHeap->array[minHeap->size - 1];
    minHeap->array[0] = lastNode;
 
    // Update position of last node
    minHeap->pos[root->v] = minHeap->size-1;
    minHeap->pos[lastNode->v] = 0;
 
    // Reduce heap size and heapify root
    --minHeap->size;
    minHeapify(minHeap, 0);
 
    return root;
}

void decreaseKey(struct MinHeap* minHeap, int v, int count_dist , int count_veh)
{
    // Get the index of v in  heap array
    int i = minHeap->pos[v];
 
    // Get the node and update its dist value
    minHeap->array[i]->count_dist = count_dist;
    minHeap->array[i]->count_veh = count_veh;
 
    // Travel up while the complete tree is not heapified.
    // This is a O(Logn) loop
    while (i && minHeap->array[i]->count_veh <= minHeap->array[(i - 1) / 2]->count_veh)
    {
        // Swap this node with its parent
        if(minHeap->array[i]->count_veh < minHeap->array[(i - 1) / 2]->count_veh){
            minHeap->pos[minHeap->array[i]->v] = (i-1)/2;
            minHeap->pos[minHeap->array[(i-1)/2]->v] = i;
            swapMinHeapNode(&minHeap->array[i],  &minHeap->array[(i - 1) / 2]);
        }

        if(minHeap->array[i]->count_veh == minHeap->array[(i - 1) / 2]->count_veh){
            if(minHeap->array[i]->count_dist < minHeap->array[(i - 1) / 2]->count_dist){
                minHeap->pos[minHeap->array[i]->v] = (i-1)/2;
                minHeap->pos[minHeap->array[(i-1)/2]->v] = i;
                swapMinHeapNode(&minHeap->array[i],  &minHeap->array[(i - 1) / 2]);
            }
        }
        // move to parent index
        i = (i - 1) / 2;
    }
}

bool isInMinHeap(struct MinHeap *minHeap, int v)
{
   if (minHeap->pos[v] < minHeap->size)
     return true;
   return false;
}

void printArr(int src, int final_dest,int dist[], int n, int parent[]){
    int i=final_dest;
    while(i!=src) {
        printf("%d - %d    %d \n", parent[i], i, dist[i]);
        i=parent[i];
    }
}


void dijkstra(struct Graph* graph, int src , int final_dest){
    int V= graph->V;
    int tot_veh[V];
    int tot_dis[V];
    int parent[V];

    struct MinHeap* minHeap=createMinHeap(V);

    for(int v=0;v<V;++v){
        tot_veh[v]=INT_MAX;
        tot_dis[v]=INT_MAX;
        minHeap->array[v]=newMinHeapNode(v,tot_dis[v],tot_veh[v]);
        minHeap->pos[v]=v;
    }

    minHeap->array[src]=newMinHeapNode(src,tot_dis[src],tot_veh[src]);
    minHeap->pos[src]=src;
    tot_dis[src]=0;
    tot_veh[src]=0;
    parent[src]=0;
    decreaseKey(minHeap,src,tot_dis[src],tot_veh[src]);

    minHeap->size=V;

    int flag=0;
    while(!isEmpty(minHeap) && flag==0){
        struct MinHeapNode* minHeapNode = extractMin(minHeap);
        int u=minHeapNode->v;

        if(u==final_dest){
            flag=1;
        }

        struct AdjListNode* pCrawl= graph->array[u].head;
        while(pCrawl != NULL){
            int v= pCrawl->dest;

            if(isInMinHeap(minHeap,v) && tot_veh[u]!=INT_MAX && pCrawl->weight_veh + tot_veh[u] < tot_veh[v]){
                tot_veh[v] = pCrawl->weight_veh+ tot_veh[u];
                tot_dis[v] = pCrawl->weight_dist + tot_dis[u];
                parent[v]=u;
                decreaseKey(minHeap,v,tot_dis[v],tot_veh[v]);
            }

            pCrawl=pCrawl->next;
        }
    }
    printArr(src,final_dest,tot_veh, V, parent);
}


int main(){
    int V=15;
    struct Graph* graph = createGraph(V);

    addEdge(graph, 0, 1, 2, 21);
    addEdge(graph, 0, 2, 4, 41);
    addEdge(graph, 2, 3, 2, 22);
    addEdge(graph, 3, 4, 3, 30);
    addEdge(graph, 4, 5, 2, 23);
    addEdge(graph, 2, 6, 3, 31);
    addEdge(graph, 6, 7, 3, 32);
    addEdge(graph, 7, 8, 2, 24);
    addEdge(graph, 7, 9, 1, 10);
    addEdge(graph, 9, 4, 2, 25);
    addEdge(graph, 6, 11, 1, 11);
    addEdge(graph, 11, 10, 2, 26);
    addEdge(graph, 10, 13, 1, 12);
    addEdge(graph, 13, 14, 3, 32);
    addEdge(graph, 11, 12, 2, 27);
    addEdge(graph, 12, 14, 3, 33);

    /*addEdge(graph, 0, 1, 1, 9);
    addEdge(graph, 0, 3, 2, 2);
    addEdge(graph, 1, 2, 3, 9);
    addEdge(graph, 2, 3, 1, 2);
    addEdge(graph, 3, 4, 3, 3);

	dijkstra(graph, 1, 3);
    */

    dijkstra(graph, 10, 14);


    return 0;
}
