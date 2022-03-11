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

;;Aka listA.get(index1).get(index2)
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

;;Helper function for remove 
(define remove_recurse(listA index i)
    (if (= i index)
        (cdr listA)
        (cons 
            (car listA) 
            (remove_recurse (cdr listA) index (+ i 1) )
        )
    )
)

;;Remove element at index: index in listA
(define remove(listA index)
    (remove_recurse listA index 0)
)

(define replace(listA index element)
    (insert (remove listA index) index element)
)

(define insert2d (listA index1 index2 element)
    (replace listA index1 (insert (nth listA index1) index2 element))
)

;;Effectively: listA[index1][index2] = element 
(define replace2d (listA index1 index2 element)
    (replace listA index1 (replace (nth listA index1) index2 element))
)

;;Utility function for printing a two-d list on multiple lines 
(define print2d (listA)
    (begin
        (set i 0)
        (while (< i (length listA))
            (begin
                (print (nth listA i))
                (set i (+ 1 i))
            )
        )
    )
)

;;Creates a two-d zero filled list of size (index1, index2)
(define zero_fill_2d (index1 index2)
    ;; insert index2 empty lists 
    (begin
        (set zero_arr ())
        (set i 0)   
        (while (< i index1)
            (begin
                (set j 0)
                (set sub_arr ())
                (while (< j index2)
                    (begin 
                        (set sub_arr (cons 0 sub_arr))
                        (set j (+ j 1))
                    )
                )
                (set zero_arr (cons sub_arr zero_arr))
                (set i (+ i 1))
            )
        )
        zero_arr
    )
)

;Adpated from https://en.wikipedia.org/wiki/Knapsack_problem
(define knapsack (values weights number_of_items capacity)
    (begin
        (set values (cons 0 values)) ;Add 0 as algo assumes array index starts at 1
        (set weights (cons 0 weights))
        (set table (zero_fill_2d (+ 1 number_of_items) (+ 1 capacity)))
        
        (set i 1)
        (while (< i (+ 1 number_of_items))
            (begin
                (set j 0)
                (while (< j (+ 1 capacity))
                (begin 
                    (if (> (nth weights i) j)
                        (set table (replace2d table i j (nth2d table (- i 1) j))) ;;Item can't fit, just copy element above
                        
                        ;;Item can fit so check if it should be included 
                        (set table (replace2d table i j ( max 
                            (nth2d table (- i 1) j)
                            (+ 
                                (nth2d table (- i 1) (- j (nth weights i)))
                                (nth values i)
                            )
                        
                        )))
                    )
                    (set j (+ 1 j))
                )
                )
                (set i (+ 1 i))  
            )
        )
        table
    )
)

;Adapted from https://www.tutorialspoint.com/printing-items-in-0-1-knapsack-in-cplusplus
(define knapsack_items (values weights number_of_items capacity)
    (begin
        (set table (knapsack values weights number_of_items capacity))

        (set items_to_include ())
        (set i 0)
        (while(< i number_of_items) ;;Zero fill items_to_include 
            (begin
                (set items_to_include (cons 0 items_to_include))
                (set i (+ i 1))
            )
        )

        (set i number_of_items)
        (set result (nth2d table number_of_items capacity))
        (while (> i 0)
            (begin 
                (if (< result 1)
                    (set i 0);;break
                    (if (= result (nth2d table (- i 1)  capacity)) ;;check if element above is different 
                        1 ;;item not included, continue
                        (begin 
                            (print (quote Include_Item:))
                            (print i)
                            (set items_to_include (replace items_to_include (- i 1) 1)) ;;Add item to list 
                            (set result (- result (nth values (- i 1)))) ;; Take away the value of the item from result 
                            (set capacity (- capacity (nth weights (- i 1)))) ;; Take away the weight of the item from capacity 
                        ) 
                    )  
                )
                (set i (- i 1))
            )    
        )
        items_to_include
    )
)


(set values (list 2 3 1 4))
(set weights (list 3 4 6 5))
(set number_of_items 4)
(set capacity 8)

(knapsack_items values weights number_of_items capacity)


;;This takes quite a while (about 3 seconds) which shows how inefficent this interpreter is
(set values (list 505 352 458 220 354 414 498 545 473 543))
(set weights (list 23 26 20 18 32 27 29 26 30 27))
(set number_of_items 10)
(set capacity 67)

(knapsack_items values weights number_of_items capacity)
;;The correctness of this result can be confirmed via: https://en.wikipedia.org/wiki/Knapsack_problem:
;;1270 == values[1] + values[4] + values[8] (list index starting at 1)
;;1270 == 505 + 220 + 545