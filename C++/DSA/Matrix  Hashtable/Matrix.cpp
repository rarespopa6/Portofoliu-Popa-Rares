#include "Matrix.h"
#include <exception>
using namespace std;

/// Θ(m)
Matrix::Matrix(int nrLines, int nrCols) {
    // nicht erlaubt
    if (nrLines <= 0 || nrCols <= 0)
        throw exception();

    this->nr_lines = nrLines;
    this->nr_cols = nrCols;

    this->m = 11;
    this->T = new Triple[this->m];

    // NULL_TELEM = leere Position
    for (int i = 0; i < this->m; ++i) {
        this->T[i].i = -1;
        this->T[i].j = -1;
        this->T[i].key = NULL_TELEM;
    }
}

/// Θ(m)
void Matrix::resize_rehash() {
    // resize
    int old_capacity = this->m;
    this->m *= 2;
    Triple *new_T = new Triple[this->m];

    for (int k = 0; k < this->m; k++) {
        new_T[k].i = -1;
        new_T[k].j = -1;
        new_T[k].key = NULL_TELEM;
    }

    // rehash
    for (int k = 0; k < old_capacity; k++) {
        // nur die Positionen, die valid sind, werden rehashed
        if (this->T[k].key != NULL_TELEM && this->T[k].key != DELETED) {
            int d = 0;
            int new_pos = this->h(this->T[k].i * 10 + this->T[k].j, d);
            // man soll eine freie Position im neuen HT finden
            while (new_T[new_pos].key != NULL_TELEM) {
                d++;
                new_pos = this->h(this->T[k].i * 10 + this->T[k].j, d);
            }
            new_T[new_pos] = this->T[k]; // das Element wird an die freie Position eingefugt
        }
    }

    // delete und reassign
    delete[] this->T;
    this->T = new_T;
}

/// Θ(1)
int Matrix::nrLines() const {
	return this->nr_lines;
}

/// Θ(1)
int Matrix::nrColumns() const {
	return this->nr_cols;
}

/// Best Case: Θ(1) - falls das Element auf der ersten Positionen der Seqvenz gehashed wird
/// Average Case: Θ(1)
/// Worst Case: Θ(m) - falls alle Elemente auf der selben Position gehashed werden
/// und das gesuchene Element an letzter Stelle dieser Seqvenz gehashed wird
/// Allgemeine Komplexitat: O(m)
TKey Matrix::element(int i, int j) const {
    // invalid
    if (i < 0 || j < 0 || i >= this->nr_lines || j >= this->nr_cols)
        throw exception();

    int d = 0;
    int pos = this->h(i * 10 + j, d);

    // man sucht die passende Position im HT (wo i und j gleich mit dem Parameters sind)
    // falls key = NULL_TELEM => es gibt sicher kein Element nach dieser Position im Seqvenz
    while (d < this->m && this->T[pos].key != NULL_TELEM) {
        // falls i und j passen und die Position nicht DELETED oder NULL_TELEM ist
        if (this->T[pos].key != NULL_TELEM && this->T[pos].key != DELETED && this->T[pos].i == i && this->T[pos].j == j) {
            return this->T[pos].key;
        }
        d++;
        pos = this->h(i * 10 + j, d); // weiter suchen => nachste Position im Seqvenz
    }
    // Es gibt keinen Element an der Position (i,j)
    return NULL_TELEM;
}

/// Best Case: Θ(1):
/// 1. Element einfugen => falls das Element auf eine der ersten Positionen der Seqvenz gehashed wird
/// 2. Element existiert schon und wird verandert => falls das Element auf eine der ersten Positionen der Seqvenz gehashed wird
/// 3. Element existiert nicht, und wird nicht eingefugt
/// 4. Element existiert und wird geloscht

/// Average Case: Θ(1)

/// Worst Case: Θ(m) - falls alle Elemente auf der selben Position gehashed werden
/// und man mochte ein Element einfugen / man braucht resize

/// Allgemeine Komplexitat: O(m)
TKey Matrix::modify(int i, int j, TKey e) {
    // invalid
    if (i < 0 || j < 0 || i >= this->nr_lines || j >= this->nr_cols) {
        throw exception();
    }

    // Falls es einen Element an die Position (i,j) gibt
    if (this->element(i, j)) {
        int d = 0;
        int pos = this->h(i * 10 + j, d); // man berechnet den initialen Hashwert (Position)

        // man sucht seine Position im HT
        // falls es eine Kollision gab, dann wurde dieser an einer der nachsten Werten(Positionen)
        // der Seqvenz im HT gespeichert
        while (d < this->m) {
            if (this->T[pos].key != NULL_TELEM && this->T[pos].key != DELETED &&
                this->T[pos].i == i && this->T[pos].j == j) {
                // Fall 1: key != NULL_TELEM und e != NULL_TELEM
                TKey old_key = this->T[pos].key; // old key
                if (e != NULL_TELEM) { // man muss nur den Wert andern
                    this->T[pos].key = e;
                }
                // Fall 2: key != NULL_TELEM und e = NULL_TELEM
                else { // man muss die Position aus der HT loschen
                    this->T[pos].key = DELETED; // markieren als DELETED
                }
                return old_key;
            }
            // weiter suchen, falls schon nicht gefunden
            d++;
            pos = this->h(i * 10 + j, d);
        }
    }
    // Falls es keinen Element an die Position (i,j) gibt
    else{
        // Fall 3: key = NULL_TELEM und e = NULL_TELEM
        if (e == NULL_TELEM) // man muss nichts tun
            return NULL_TELEM;

        // Fall 4: key = NULL_TELEM und e != NULL_TELEM
        // man muss den Wert im HT einfugen
        int d = 0;
        int pos = this->h(i * 10 + j, d);
        // man sucht eine freie Position im HT
        while (d < this->m && this->T[pos].key != NULL_TELEM && this->T[pos].key != DELETED) {
            d++;
            pos = this->h(i * 10 + j, d);
        }
        // Falls man eine freie Position gefunden hat
        if (d < this->m && this->T[pos].key == NULL_TELEM || this->T[pos].key == DELETED) {
            this->T[pos].key = e;
            this->T[pos].i = i;
            this->T[pos].j = j;
            return NULL_TELEM; // old_key - vorher war nichts dort
        }
        // Es gibt keine freie Position im HT => resize und rehash
        else {
            this->resize_rehash();
            return this->modify(i, j, e); // man soll die Funktion wieder rufen, weil e nicht eingefugt wurde
        }
    }
}

bool Matrix::operator==(const Matrix &other) const {
    if (this->nr_lines != other.nr_lines || this->nr_cols != other.nr_cols) {
        return false;
    }

    for (int k = 0; k < this->m; k++) {
        if (this->T[k].key != NULL_TELEM && this->T[k].key != DELETED) {
            bool found = false;
            int p = 0;
            int pos = other.h(this->T[k].i * 10 + this->T[k].j, p);
            while (p < other.m && other.T[pos].key != NULL_TELEM && !found) {
                if (other.T[pos].i == this->T[k].i && other.T[pos].j == this->T[k].j &&
                    other.T[pos].key == this->T[k].key) {
                    found = true;
                }
                p++;
                pos = other.h(this->T[k].i * 10 + this->T[k].j, p);
            }
            if (!found) {
                return false;
            }
        }
    }

    for (int k = 0; k < other.m; k++) {
        if (other.T[k].key != NULL_TELEM && other.T[k].key != DELETED) {
            bool found = false;
            int p = 0;
            int pos = this->h(other.T[k].i * 10 + other.T[k].j, p);
            while (p < this->m && this->T[pos].key != NULL_TELEM && !found) {
                if (this->T[pos].i == other.T[k].i && this->T[pos].j == other.T[k].j &&
                    this->T[pos].key == other.T[k].key) {
                    found = true;
                }
                p++;
                pos = this->h(other.T[k].i * 10 + other.T[k].j, p);
            }
            if (!found) {
                return false;
            }
        }
    }
    return true;
}


/// Θ(1)
Matrix::~Matrix() {
    delete[] this->T; // delete Hashtable
}


