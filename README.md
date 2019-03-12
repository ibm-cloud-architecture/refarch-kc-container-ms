# Container management microservice

This project is part of the container shipment implementation solution you can read detail [here.](https://ibm-cloud-architecture.github.io/refarch-kc/).

The goal of this Container management service is to support the reefer containers inventory management and to process all the events related to the container entity. To read the best practices and the "how tos" relate to the different implementation we are doing see the book [format here](https://ibm-cloud-architecture.github.io/refarch-kc-container-ms). 

Update 03/12/2019.

### Building this booklet locally

The content of this repository is written with markdown files, packaged with [MkDocs](https://www.mkdocs.org/) and can be built into a book-readable format by MkDocs build processes.

1. Install MkDocs locally following the [official documentation instructions](https://www.mkdocs.org/#installation).
2. `git clone https://github.com/ibm-cloud-architecture/refarch-kc-container-ms.git` _(or your forked repository if you plan to edit)_
3. `cd refarch-kc-container-ms`
4. `mkdocs serve`
5. Go to `http://127.0.0.1:8000/` in your browser.

### Pushing the book to GitHub Pages

1. Ensure that all your local changes to the `master` branch have been committed and pushed to the remote repository.
   1. `git push origin master`
2. Ensure that you have the latest commits to the `gh-pages` branch, so you can get others' updates.
	```bash
	git checkout gh-pages
	git pull origin gh-pages
	
	git checkout master
	```
3. Run `mkdocs gh-deploy` from the root refarch-da directory.

--- 

## Contribute

We welcome your contributions. There are multiple ways to contribute: report bugs and improvement suggestion, improve documentation and contribute code.
We really value contributions and to maximize the impact of code contributions we request that any contributions follow these guidelines:

The [contributing guidelines are in this note.](./CONTRIBUTING.md)

## Project Status
03/19 Starting