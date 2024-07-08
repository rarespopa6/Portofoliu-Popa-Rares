
#include <exception>
#include "ListIterator.h"
#include "IteratedList.h"

using namespace std;

/// Θ(1)
/// Constructor => leere Liste
IteratedList::IteratedList() {
	this->length = 0;
    this->head = nullptr;
    this->tail = nullptr;

}

/// Θ(1)
int IteratedList::size() const {
	return this->length;
}

/// Θ(1)
bool IteratedList::isEmpty() const {
	return this->length == 0;
}

/// Θ(1)
ListIterator IteratedList::first() const {
	return ListIterator(*this);
}

/// Θ(1)
TElem IteratedList::getElement(ListIterator pos) const {
    if (pos.valid())
        return pos.current->info;
    throw exception();
}

/// Θ(1)
TElem IteratedList::remove(ListIterator& pos) {
	if (pos.valid()){
        TElem elemToDelete = pos.current->info;

        /// Current node = head
        if (pos.current == this->head){
            /// Die Liste enthalt nur einen Ellement => head und tail werden geloscht
            if (pos.current == this->tail){
                this->head = nullptr;
                this->tail = nullptr;
            }
            else{ /// Head node wird geloscht und head wird das zweite Node aus der Liste sein
                this->head = this->head->next;
                this->head->prev = nullptr;
            }
        } /// Current node = tail => man loscht tail, tail wird der vorletzte Node sein
        else if (pos.current == this->tail){
            this->tail = this->tail->prev;
            this->tail->next = nullptr;
        }
        else{ /// Current node steht irgendwo in der Liste
            pos.current->next->prev = pos.current->prev;
            pos.current->prev->next = pos.current->next;

            delete pos.current; /// Node wird geloscht
        }
        this->length--;  /// Lange der IteratedList -= 1
        return elemToDelete; /// das Ellement das geloscht wurde
    }
    throw exception(); /// invalid Iterator
}

/// Best: Θ(1) - wenn der gesuchte Node head ist
/// Average: Θ(n)
/// Worst: Θ(n) - wenn der gesuchte Node tail ist oder existier nich in der Liste
/// Allgemeine Komplexitat: O(n)
ListIterator IteratedList::search(TElem e) const{
    /// Man sucht den Node mit info = e
    Node *searched_node = this->head;
    while (searched_node != nullptr && searched_node->info != e)
        searched_node = searched_node->next;

    /// Man initialisiert ein iterator
    ListIterator iterator(*this);

    /// Iterator wird auf den gefundenen Node anzeigen, falls valid
    if (searched_node != nullptr)
        iterator.set_node(searched_node);
    else /// Node nicht gefunden => NIL
        iterator.set_node(nullptr);

    return iterator;
}

/// Θ(1)
TElem IteratedList::setElement(ListIterator pos, TElem e) {
    if (pos.valid()){
        /// Neues Wert fur current Node aus ListIterator
        TElem old_info = pos.current->info;
        pos.current->info = e;
        return old_info;
    }
    throw exception();
}

/// Θ(1)
void IteratedList::addToBeginning(TElem e) {
    /// Neues head
    Node *new_node = new Node;
    new_node->info = e;
    new_node->next = this->head;
    new_node->prev = nullptr;

    if (this->head != nullptr)
        this->head->prev = new_node;
    else /// empty => head = tail
        this->tail = new_node;

    this->head = new_node;
    this->length++;
}

/// Θ(1)
/// Add nach der Position, dass ListIterator zeigt
void IteratedList::addToPosition(ListIterator& pos, TElem e) {
    if (pos.current == nullptr) /// uberprufe ob invalid
        throw exception();

    /// Add nach tail
    if (pos.current == this->tail){
        this->addToEnd(e);
        return;
    }

    /// Fur head funktioniert es auch so. (add nach head)

    Node *new_node = new Node;
    new_node->info = e;
    new_node->next = pos.current->next;
    new_node->prev = pos.current;
    pos.current->next->prev = new_node;
    pos.current->next = new_node;

    /// Man wird den current Node auf den neuen Node, dass hinzugefugt wurde verschoben
    pos.current = new_node;
    this->length++;
}

/// Θ(1)
void IteratedList::addToEnd(TElem e) {
    /// Neues tail
    Node *new_node = new Node;
    new_node->info = e;
    new_node->next = nullptr;
    new_node->prev = this->tail;

    /// empty List
    if (this->head == nullptr){
        this->head = new_node;
        this->tail = new_node;
    }
    else{ /// nicht empty
        this->tail->next = new_node;
        this->tail = new_node;
    }
    this->length++;
}

/// Θ(n)
void IteratedList::reverse() {
    /// Man fangt von head an
    Node *current = this->head;
    Node *temp;

    /// Verbindungen wachseln fur die ganze Liste
    while (current != nullptr){
        temp = current->prev;
        current->prev = current->next;
        current->next = temp;
        current = current->prev;
    }

    /// Wachseln head und tail
    temp = this->head;
    this->head = this->tail;
    this->tail = temp;
}


/// Θ(n)
/// Destructor
IteratedList::~IteratedList() {
    Node* node_to_delete = this->head;  /// Anfang der Liste

    /// Geht durch alle Nodes und loscht diese
    while (node_to_delete != nullptr){
        Node *next_node = node_to_delete->next;
        delete node_to_delete;
        node_to_delete = next_node;
    }

    /// Um sicher zu sein, dass head und tail keine invalide Verbindungen mehr haben
    this->head = nullptr;
    this->tail = nullptr;
}
