(ns ^:figwheel-no-load numguess.dev
  (:require
    [numguess.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
