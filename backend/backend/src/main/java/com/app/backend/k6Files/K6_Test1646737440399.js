import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '2s', target: 2},
		{ duration: '1s', target: 5},
		{ duration: '1s', target: 8},
 	],
};

export default function () {
	const BASE_URL = 'http://localhost:80/SentimentAnalyzer';

	const responses = http.batch([
		['GET', `${BASE_URL}`, null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}