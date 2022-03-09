;; Returns the length of the list
(define length (listA)
    (begin
        (set len 0)
        (while (list? listA)
            (begin 
                (set len (+ len 1))
                (set listA (cdr listA))
            )
        )
        len
    )
)

;; Returns the nth element of the list
(define nth (listA index)
    (begin
        (set i 0)
        (while (null? (= i index))
            (begin 
                (set listA (cdr listA))
                (set i (+ i 1))
            )
        )
        (car listA)
    )
)

;;Helper function for insert 
(define insert_recurse (listA index element i)
    (if (= i index)
        (cons element listA)
        (cons 
            (car listA) 
            (insert_recurse (cdr listA) index element (+ i 1) )
        )
    )
)

;;Returns the list with the element inserted at the given index
(define insert (listA index element)
    (insert_recurse listA index element 0)
)

;; Simple insertion sort algorithm. Returns a sorted list.
(define insertion_sort(listA)
    (begin
        (set sorted (list (car listA)))
        (set i 1)
        (while (< i (length listA))
            (begin
                (set current (nth listA i))
                (set j 0)
                (set insertIndex (length sorted))
                (while (< j (length sorted))
                    (if (< current (nth sorted j))
                        (begin 
                            (set insertIndex j)
                            (set j (length sorted)) ;;aka break
                        )
                        (set j (+ j 1))
                    ) 
                )
                (set sorted (insert sorted insertIndex current))
                (set i (+ i 1))
            )
        )
        sorted
    )
)

(length (list 1 2 3))
(nth (list 1 2 3) 1)
(insert (list 1 2 3) 1 15)
(insertion_sort (list 8 4 5 7 3 2 5 7 0))