import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '5s', target: 7},
		{ duration: '3s', target: 6},
		{ duration: '1s', target: 4},
 	],
};

export default function () {
	const BASE_URL = 'https://jsonplaceholder.typicode.com/users';

	const responses = http.batch([
		['GET', `${BASE_URL}`, null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}