/**
 * Interfaces and Enums for Elasticsearch queries
 */

export interface Query {
    index: string;
    body: QueryBody;
}

export interface QueryBody {
    track_total_hits: boolean;
    _source?: Array<string>;
    sort?: Object;
    query: BoolQuery;
    from?: number;
    size?: number;
}

export interface BoolQuery {
    bool: QueryContext
}

export interface QueryContext {
    filter?: (QueryFragment | BoolQuery)[],
    must?: (QueryFragment | BoolQuery)[],
    must_not?: (QueryFragment | BoolQuery)[],
    should?: (QueryFragment | BoolQuery)[],
}

export enum QueryFragmentType {
    term = "term",
    terms = "terms",
    match = "match",
    exists = "exists"
}

export interface QueryFragment {
    [QueryFragmentType.term]?: { [key: string]: any },
    [QueryFragmentType.terms]?: { [key: string]: any },
    [QueryFragmentType.match]?: { [key: string]: any },
    [QueryFragmentType.exists]?: { [key: string]: any }
}
