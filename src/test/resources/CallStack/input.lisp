(define error (listA)
    (car listA)
)

(define not_in_stack ()
    1
)

(define bar ()
    (begin 
        (not_in_stack)
        (error 1)
    )
)

(define foo ()
    (bar)
)

(foo)
