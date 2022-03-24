(define for_each (listA i body)
    (while (null? (null? listA))
    (begin
        (set i (car listA))
        (set listA (cdr listA))
        (eval body)
    )    
    )
)

(define foo ()
(begin 
    (set sorted t)
    (for_each (list 1 2 3) ()
        (quote (set sorted ()))
    )
    sorted
)
)


(foo)


;;The environemnt is saved when the function is defined! not when it is called. Do I want that? 