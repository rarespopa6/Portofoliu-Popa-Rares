#pragma once
//DO NOT INCLUDE SETITERATOR

//DO NOT CHANGE THIS PART
typedef int TElem;
typedef TElem TComp;
typedef bool(*Relation)(TComp, TComp);
#define NULL_TELEM -11111
class SortedSetIterator;

struct BSTNode {
    TComp info;
    BSTNode* left;
    BSTNode* right;

    explicit BSTNode(TComp info, BSTNode* left = nullptr, BSTNode* right = nullptr) {
        this->info = info;
        this->left = left;
        this->right = right;
    }
};


class SortedSet {
    friend class SortedSetIterator;

private:
    BSTNode* root;
    Relation relation;

public:
	//constructor
	SortedSet(Relation r);

	//adds an element to the sorted set
	//if the element was added, the operation returns true, otherwise (if the element was already in the set) 
	//it returns false
	bool add(TComp e);

	
	//removes an element from the sorted set
	//if the element was removed, it returns true, otherwise false
	bool remove(TComp e);

	//checks if an element is in the sorted set
	bool search(TElem elem) const;


	//returns the number of elements from the sorted set
	int size() const;

	//checks if the sorted set is empty
	bool isEmpty() const;

	//returns an iterator for the sorted set
	SortedSetIterator iterator() const;

	// destructor
	~SortedSet();


    bool search_rec(BSTNode *node, TComp elem) const;

    BSTNode *insert_rec(BSTNode *node, TComp e);

    void destroy_rec(BSTNode *node);
};
