var a = {
	"post_filter" : {
		"terms" : {
			"category" : [ 1, 5, 7, 8, 9, 10, 11, 12, 14, 15, 16, 157, 159,
					160, 161, 162, 201, 202, 203, 204, 205, 206, 207, 208, 209,
					210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221,
					25000, 25001, 25002, 25003, 25100 ],
			"manufacturer" : [ 5353 ]
		}
	},
	"query" : {
		"function_score" : {
			"functions" : [ {
				"script_score" : {
					"lang" : "groovy",
					"script_file" : "qscore"
				}
			} ],
			"query" : {
				"query_string" : {
					"query" : "CCK-8",
					"minimum_should_match" : 1,
					"use_dis_max" : true,
					"fields" : [ "pname^20" ]
				}
			}
		}
	},
	"facets" : {
		"category" : {
			"terms" : {
				"field" : "category",
				"size" : 2147483647
			},
			"facet_filter" : {
				"terms" : {
					"category" : [ 1, 5, 7, 8, 9, 10, 11, 12, 14, 15, 16, 157,
							159, 160, 161, 162, 201, 202, 203, 204, 205, 206,
							207, 208, 209, 210, 211, 212, 213, 214, 215, 216,
							217, 218, 219, 220, 221, 25000, 25001, 25002,
							25003, 25100 ],
					"manufacturer" : [ 5353 ]
				}
			}
		},
		"manufacturer" : {
			"terms" : {
				"field" : "manufacturer",
				"size" : 2147483647
			},
			"facet_filter" : {
				"terms" : {
					"category" : [ 1, 5, 7, 8, 9, 10, 11, 12, 14, 15, 16, 157,
							159, 160, 161, 162, 201, 202, 203, 204, 205, 206,
							207, 208, 209, 210, 211, 212, 213, 214, 215, 216,
							217, 218, 219, 220, 221, 25000, 25001, 25002,
							25003, 25100 ],
					"manufacturer" : [ 5353 ]
				}
			}
		}
	}
}

var b = {
	"query" : {
		"function_score" : {
			"functions" : [ {
				"script_score" : {
					"script_file" : "qscore",
					"lang" : "groovy"
				}
			} ],
			"query" : {
				"query_string" : {
					"minimum_should_match" : 1,
					"use_dis_max" : true,
					"query" : "CCK-8",
					"fields" : [ "pname^20" ]
				}
			}
		}
	},
	"facets" : {
		"category" : {
			"facet_filter" : {
				"terms" : {
					"category" : [ 5, 7, 201, 202, 203, 204, 210, 25100, 8, 12,
							14, 15, 16, 216, 217, 218, 219, 220, 221, 9, 157,
							159, 160, 161, 162, 10, 205, 206, 207, 208, 209,
							211, 212, 11, 213, 214, 215, 25000, 25001, 25002,
							25003, 25150, 1 ],
					"manufacturer" : [ 5353 ]
				}
			},
			"terms" : {
				"field" : "category",
				"size" : 2147483647
			}
		},
		"manufacturer" : {
			"facet_filter" : {
				"terms" : {
					"category" : [ 5, 7, 201, 202, 203, 204, 210, 25100, 8, 12,
							14, 15, 16, 216, 217, 218, 219, 220, 221, 9, 157,
							159, 160, 161, 162, 10, 205, 206, 207, 208, 209,
							211, 212, 11, 213, 214, 215, 25000, 25001, 25002,
							25003, 25150, 1 ],
					"manufacturer" : [ 5353 ]
				}
			},
			"terms" : {
				"field" : "manufacturer",
				"size" : 2147483647
			}
		}
	},
	"post_filter" : {
		"terms" : {
			"category" : [ 5, 7, 201, 202, 203, 204, 210, 25100, 8, 12, 14, 15,
					16, 216, 217, 218, 219, 220, 221, 9, 157, 159, 160, 161,
					162, 10, 205, 206, 207, 208, 209, 211, 212, 11, 213, 214,
					215, 25000, 25001, 25002, 25003, 25150, 1 ],
			"manufacturer" : [ 5353 ]
		}
	}
}