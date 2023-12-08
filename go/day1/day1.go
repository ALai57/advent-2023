package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"

	"github.com/samber/lo"
)

var basicConversion = map[string]string{
	"0": "0",
	"1": "1",
	"2": "2",
	"3": "3",
	"4": "4",
	"5": "5",
	"6": "6",
	"7": "7",
	"8": "8",
	"9": "9",
}

var advancedConversion = lo.Assign(map[string]string{
	"zero":  "0",
	"one":   "1",
	"two":   "2",
	"three": "3",
	"four":  "4",
	"five":  "5",
	"six":   "6",
	"seven": "7",
	"eight": "8",
	"nine":  "9",
}, basicConversion)

// 1abc2
func parseLine(conversion map[string]string, s string) int {
	re := strings.Join(lo.Keys(conversion), "|")

	var regexPattern = regexp.MustCompile("^(" + re + ")")

	matches := []string{}
	for i := 0; i < len(s); i++ {
		m := regexPattern.FindString(s[i:])
		if m != "" {
			matches = append(matches, conversion[m])
		}
	}

	num := string(matches[0] + matches[len(matches)-1])
	n, _ := strconv.Atoi(num)
	return n
}

func main() {
	b, err := os.ReadFile("../../clojure/resources/day1.txt")
	if err != nil {
		panic("Error reading file")
	}

	lines := strings.Split(strings.TrimSpace(string(b)), "\n")

	// Part 1
	result := 0
	for _, line := range lines {
		result += parseLine(basicConversion, line)
	}
	fmt.Println(result)

	// Part 2
	result2 := 0
	for _, line := range lines {
		result2 += parseLine(advancedConversion, line)
	}
	fmt.Println(result2)
}
