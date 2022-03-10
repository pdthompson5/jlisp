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

(define nth2d (listA index1 index2)
    (nth (nth listA index1) index2)
)

(define max (intA intB)
    (if (> intA intB)
        intA
        intB
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

(define remove_recurse(listA index i)
    (if (= i index)
        (cdr listA)
        (cons 
            (car listA) 
            (remove_recurse (cdr listA) index (+ i 1) )
        )
    )
)

(define remove(listA index)
    (remove_recurse listA index 0)
)

(define replace(listA index element)
    (insert (remove listA index) index element)
)

(define insert2d (listA index1 index2 element)
    (replace listA index1 (insert (nth listA index1) index2 element))
)

(define replace2d (listA index1 index2 element)
    (replace listA index1 (replace (nth listA index1) index2 element))
)



;; (define knapsack (values weights item_number capacity)
;;     (begin
;;         (set table ())
;;         (set i 0)
;;         (while (i ))
        
;;     )
;; )


(set table (list (list 1 2 3) (list 1 2 3) (list 1 2 3)))
table
(nth2d table 0 2)



(insert2d table 0 2 15)
(replace2d table 0 2 15)



;; (set test (list 1 2 3))
;; (set test2 test)

;; test 
;; test2

;; (set test (cdr test))

;; test 
;; test2




