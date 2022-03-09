;; (define fib(n)
;;     (if (= n 0)
;;         (0)
;;         (if (= n 1)
;;             (1)
;;             (+ (fib (- n 1)) (fib (- n 2)))
;;         )
;;     )
;; )

;; (fib 8)


;; (define count(n)
;;     (if (> n 10)
;;         (2)
;;         (+ (count (+ n 1)) (count (+ n 1)))
;;     )
;; )

;; (count 3)

;; TODO: Fix this not parsing 
;; (set i 2)
;; (set element 3)
;; (set listA (list 1 2 3))
;; (set index 2)





;; ;; Helper function for insert 
(define insert_recurse(listA index element i)
    (begin
        (if (= i index)
            (cons element listA)
            (cons 
                (car listA) 
                (insert_recurse (cdr listA) index element (+ i 1) )
            )
        )
    )
)

;; (define insert (listA index element)
;;     (insert_recurse listA index element 0)
;; )


;; (insert (list 1 2 3) 1 15)





