import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '3s', target: 2},
 	],
};

export default function () {
 const BASE_URL = 'https://www.google.com';

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}