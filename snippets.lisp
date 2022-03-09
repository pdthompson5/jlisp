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


(length (list 1 2 3))
(nth (list 1 2 3) 1)
(insert (list 1 2 3) 1 15)






