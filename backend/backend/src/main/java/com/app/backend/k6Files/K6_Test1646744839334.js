import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '2s', target: 5},
		{ duration: '2s', target: 3},
		{ duration: '1s', target: 10},
 	],
};

export default function () {
	const BASE_URL = 'https://jsonplaceholder.typicode.com/users';

	const responses = http.batch([
		['GET', `${BASE_URL}`, null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}