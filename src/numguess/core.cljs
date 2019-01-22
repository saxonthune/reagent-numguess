(ns numguess.core
    (:require
      [reagent.core :as r]))

(defonce answer (r/atom (inc (rand-int 100))))
(defonce guess-count (r/atom 0))
(defonce table-contents (r/atom []))
(defonce guess-field (r/atom -1))

;; todo: functions handling atoms/derefs should take
;; 		 atoms/derefs as parameters
(defn insert-row 
"Creates map with data for a single guess, then updates
atom (vector of maps) by appending the map."
	[value]
	(let [row 
			{:id (swap! guess-count inc) 
			 :content (str value)
			 :cmp-result (cmp-guess @guess-field @answer)}]
		(swap! table-contents conj row)))

(defn del-last-row 
"Removes most recent guess from table-contents 
atom; currently unused"
	[]
	(let [end (dec (count @table-contents))]
		(swap! table-contents subvec 0 end)))

(defn del-all-rows []
	(reset! table-contents [])
	(reset! guess-count 0))

(defn cmp-guess 
"Returns string with quality of guess compared to
true answer"
	[guess ans]
	(cond 
		(< guess ans) "Too Low!"
		(> guess ans) "Too High!"
		:else "You Win!"))

(defn reset-game 
"Resets atoms related to game state in order
to start new round"
	[]
	(del-all-rows)
	(reset! answer (inc (rand-int 100))))

(defn guess-input [] 
	[:div
	[:input {:type "number"
			 :placeholder "Enter a Number"
			 :on-change #(reset! guess-field (-> % .-target .-value))}]
	[:input {:type "button"
			 :value "Guess"
			 :on-click #(insert-row @guess-field)}]])

(defn guess-table []
	[:div {:id "guess-table"}
	[guess-input]
	[:input {:type "button"
			 :value "Reset"
			 :on-click #(reset-game)}]
	[:table
		[:thead [:tr
					[:th "Attempt #"]
					[:th "Guess"]
					[:th "Result"]]]
		[:tbody
			(for [item @table-contents]
				^{:key (:id item)}
				[:tr	
					[:td (:id item)]
					[:td (:content item)]
					[:td (:cmp-result item)]])]]])

(defn intro []
  [:div {:id "intro"}
  [:h2 "Guess the Number"]
  [:p "An integer number has been randomly selected
			between 1 and 100.  Use the field below to enter
			guesses.  A table will keep track of your guesses
			and whether the guess was higher or lower than the
			actual number."]])

(defn footer []
	[:div {:class "footer"}
	[:p {:class "website"} [:a {:href "https://saxon.zone"} "saxon.zone"]]
	[:p {:class "github"} "Source on " [:a 
			{:href "https://github.com/saxonthune/reagent-numguess"}
			"Github"]]])

(defn content []
	[:div {:class "wrapper"}
	[intro]
	[guess-table]
    [footer]])

(defn init! []
	(r/render [content] (.getElementById js/document "app")))
