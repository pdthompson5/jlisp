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

;; Helper function for insert 
(define insert_recurse(listA index element i)
    (begin
        (if (= i index)
            (cons element listA)
            (cons 
                (car listA) 
                (insert_recurse((cdr listA) index element (+ i 1)))
            )
        )
    )
)

(define insert (listA index element)
    (insert_recurse listA index element 0)
)


(length (list 1 2 3))
(nth (list 1 2 3) 1)
(insert (list 1 2 3) 1 15)






;; (define insertion_sort (listA) (
;;     (begin 
;;         (let )
;;     )
;; ))