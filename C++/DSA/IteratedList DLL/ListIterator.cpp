#include "ListIterator.h"
#include "IteratedList.h"
#include <exception>

using namespace std;

/// Θ(1)
/// Zeigt an ersten Node (head)
ListIterator::ListIterator(const IteratedList& list) : list(list) {
    this->current = this->list.head;
}

/// Θ(1)
/// Zeigt an ersten Node (head)
void ListIterator::first() {
    this->current = this->list.head;
}

/// Θ(1)
/// Zeigt den nachsten Node aus der Liste, falls valid
void ListIterator::next() {
    if(this->valid()) {
        this->current = this->current->next;
        return ;
    }
    throw exception();
}

/// Θ(1)
/// Valid nur wenn current Node != NIL ist
bool ListIterator::valid() const {
	return this->current != nullptr;
}

/// Θ(1)
/// Das Wert befindet sich in current->info
TElem ListIterator::getCurrent() const {
    if(this->valid())
	    return this->current->info;
    throw exception();
}

/// Θ(1)
/// Set Node => current wird auf den gegebenen Node zeigen
/// Notwendig fur die search Funktion aus IteratedList
void ListIterator::set_node(IteratedList::Node *node) {
    this->current = node;
}

