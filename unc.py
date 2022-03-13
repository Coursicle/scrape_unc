def scrape(self):

	# init libraries
	import requests
	from bs4 import BeautifulSoup

	# init a session
	session = requests.Session()

	# get the initial page
	session.get("https://reports.unc.edu/class-search/tiled/")

	# additional requests that update the TS399d25a7027 cookie, which seems necessary
	session.get("https://reports.unc.edu/static/public/course_search/course_search.css")
	session.get("https://reports.unc.edu/static/public/course_search/utilities.js")
	session.get("https://reports.unc.edu/static/public/course_search/icon.png")
	session.get("https://reports.unc.edu/static/public/course_search/unc-logo.png")
	session.get("https://reports.unc.edu/static/public/course_search/course_search.js")

	# refresh the page, this is done by the browser so we do it too.
	searchPage = session.get("https://reports.unc.edu/class-search/tiled/")

	# create the search page soup
	soup = BeautifulSoup(searchPage.content)

	# pull one of the csrf tokens, which is in a hidden field on the page
	token = soup.find("input", {"name":"csrfmiddlewaretoken"})['value']

	# use all of the same headers that are used by the browser
	headers = {
			'Connection': 'keep-alive',
			'Pragma': 'no-cache',
			'Cache-Control': 'no-cache',
			'sec-ch-ua': '" Not A;Brand";v="99", "Chromium";v="99", "Google Chrome";v="99"',
			'sec-ch-ua-mobile': '?0',
			'sec-ch-ua-platform': '"macOS"',
			'Upgrade-Insecure-Requests': '1',
			'Origin': 'https://reports.unc.edu',
			'Content-Type': 'application/x-www-form-urlencoded',
			'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36',
			'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
			'Sec-Fetch-Site': 'same-origin',
			'Sec-Fetch-Mode': 'navigate',
			'Sec-Fetch-User': '?1',
			'Sec-Fetch-Dest': 'document',
			'Referer': 'https://reports.unc.edu/class-search/',
			'Accept-Language': 'en-US,en;q=0.9',
	}

	# input the token and try pulling AAAD classes in the Spring 2022 term
	data = {
		'csrfmiddlewaretoken': token,
		'term': '2222',
		'subject': 'AAAD',
		'catalog_number': '',
		'filter-submit': ''
	}

	# make the post request
	courses = session.post('https://reports.unc.edu/class-search/tiled/', headers=headers, data=data)

	# print as a test
	print courses.content
	





	
	# TODO
	return allCourses
